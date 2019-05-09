package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.videoinfo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;


import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.Thumbnail;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.VideoInformation;
import net.ddns.andrewnetwork.ludothornsoundbox.databinding.DialogVideoBinding;
import net.ddns.andrewnetwork.ludothornsoundbox.di.component.ActivityComponent;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.videoinfo.VideoInformationViewPresenterBinder.IVideoInformationPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.videoinfo.VideoInformationViewPresenterBinder.IVideoInformationView;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.AudioUtils;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.CommonUtils;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.JsonUtil;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.ListUtils;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.StringUtils;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.view.ReducedDialogFragment;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;

import com.google.gson.reflect.TypeToken;

import java.util.List;

import static net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.HomeFragment.KEY_LOAD_AT_ONCE;
import static net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.controller.VideoManager.buildVideoUrl;

public class VideoInformationFragment extends ReducedDialogFragment implements IVideoInformationView {

    private static final String KEY_AUDIO = "KEY_AUDIO";
    private static final String KEY_VIDEO_LIST = "KEY_VIDEO_LIST";
    private Bundle savedInstanceState;
    private DialogVideoBinding mBinding;
    private LudoAudio audio;

    @Inject
    IVideoInformationPresenter<IVideoInformationView> mPresenter;
    private boolean loadAtOnce;

    public static VideoInformationFragment newInstance(boolean audioAtonce, LudoAudio audio, List<LudoAudio> audioList) {

        VideoInformationFragment videoInformationFragment = new VideoInformationFragment();
        Bundle bundle = new Bundle();

        bundle.putString(KEY_AUDIO, JsonUtil.getGson().toJson(audio));
        bundle.putString(KEY_VIDEO_LIST, JsonUtil.getGson().toJson(audioList));
        bundle.putBoolean(KEY_LOAD_AT_ONCE, audioAtonce);

        videoInformationFragment.setArguments(bundle);

        return videoInformationFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        audio = getArguments() != null ? JsonUtil.getGson().fromJson(getArguments().getString(KEY_AUDIO), LudoAudio.class) : new LudoAudio();

        if (getArguments() != null) {
            audio.getVideo().setAudioList(JsonUtil.getGson().fromJson((String) getArguments().get(KEY_VIDEO_LIST), new TypeToken<List<LudoAudio>>(){}.getType()));
            loadAtOnce = getArguments().getBoolean(KEY_LOAD_AT_ONCE);
        }
        else {
            loadAtOnce = false;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_video, container, false);

        ActivityComponent activityComponent = getActivityComponent();
        if(activityComponent != null) {
            activityComponent.inject(this);
            mPresenter.onAttach(this);
        }

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(!loadAtOnce) {
            mPresenter.getVideoInformation(audio);
        } else {
            setVideo(getActivity(), audio.getVideo());
        }
    }

    private void setVideo(Context context, LudoVideo video) {
        VideoInformation videoInformation = video.getVideoInformation() != null ? video.getVideoInformation() : new VideoInformation();
        if(video.getThumbnail() != null && video.getThumbnail().getImage() != null) {
            mBinding.thumbnailImage.setImageBitmap(video.getThumbnail().getImage());
        } else {
            mPresenter.getThumbnail(video);
        }
        mBinding.titleLabel.setText(video.getTitle());

        mBinding.likes.setText(StringUtils.valueOf(videoInformation.getLikes()));
        mBinding.dislikes.setText(StringUtils.valueOf(videoInformation.getDislikes()));
        mBinding.viewsLabel.setText(StringUtils.valueOf(videoInformation.getViews()));
        mBinding.descrizioneLabel.setText(video.getDescription());
        if(!ListUtils.isEmptyOrNull(video.getConnectedAudioList())) {

            mBinding.audioListLabel.setVisibility(View.VISIBLE);

            for (LudoAudio audio : video.getConnectedAudioList()) {
                Button button = new Button(context);
                button.setText(audio.getTitle());
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                        (int) ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                button.setLayoutParams(params);
                button.setOnClickListener(v -> AudioUtils.playTrack(context, audio, null));

                mBinding.audioListLayout.addView(button);
            }
        }

        mBinding.thumbnailImage.setOnClickListener(v -> CommonUtils.openLink(getContext(), buildVideoUrl(video.getId())));
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public Bundle getSavedInstanceState() {
        return savedInstanceState;
    }

    @Override
    protected ProgressBar getProgressDialog() {
        return mBinding.progressDialog;
    }

    @Override
    protected View getProgressBackground() {
        return mBinding.progressBackground;
    }

    @Override
    public void onThumbnailLoadSuccess(Thumbnail thumbnail) {
        mBinding.thumbnailImage.setImageBitmap(thumbnail.getImage());
    }

    @Override
    public void onThumbnailLoadFailed() {
        showDialog("Ooops! Non sono riuscito a caricare la thumbnail di questo video.");
    }

    @Override
    public void onVideoInformationLoadFailed() {
        showDialog("Ooops! Non sono riuscito a caricare le informazioni di questo video.", (dialog, which) -> dismiss(), false);

    }

    @Override
    public void onVideoInformationLoadSuccess(LudoAudio audio) {
        setVideo(getActivity(), audio.getVideo());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setRetainInstance(true);
    }
}
