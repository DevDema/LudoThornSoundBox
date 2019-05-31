package net.ddns.andrewnetwork.ludothornsoundbox.ui.videoinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.gson.reflect.TypeToken;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.Thumbnail;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.VideoInformation;
import net.ddns.andrewnetwork.ludothornsoundbox.databinding.DialogVideoBinding;
import net.ddns.andrewnetwork.ludothornsoundbox.di.component.ActivityComponent;

import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.AdsActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.videoinfo.VideoInformationViewPresenterBinder.IVideoInformationPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.videoinfo.VideoInformationViewPresenterBinder.IVideoInformationView;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.AudioUtils;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.CommonUtils;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.JsonUtil;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.ListUtils;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.StringUtils;

import java.util.List;

import javax.inject.Inject;

import static net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.HomeFragment.KEY_LOAD_AT_ONCE;
import static net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.controller.VideoManager.buildVideoUrl;
import static net.ddns.andrewnetwork.ludothornsoundbox.utils.StringUtils.nonEmptyNonNull;

public class VideoInformationActivity extends AdsActivity implements IVideoInformationView {

    private static final String KEY_AUDIO = "KEY_AUDIO";
    private static final String KEY_VIDEO_LIST = "KEY_VIDEO_LIST";
    private DialogVideoBinding mBinding;

    @Inject
    IVideoInformationPresenter<IVideoInformationView> mPresenter;

    public static void newInstance(Context context, boolean audioAtonce, LudoAudio audio, List<LudoAudio> audioList) {

        Intent intent = new Intent(context, VideoInformationActivity.class);

        intent.putExtra(KEY_AUDIO, JsonUtil.getGson().toJson(audio));
        intent.putExtra(KEY_VIDEO_LIST, JsonUtil.getGson().toJson(audioList));
        intent.putExtra(KEY_LOAD_AT_ONCE, audioAtonce);

        context.startActivity(intent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityComponent activityComponent = getActivityComponent();
        if (activityComponent != null) {
            activityComponent.inject(this);
            mPresenter.onAttach(this);
        }

        if (getSupportActionBar() != null) {
            mIsHomeButtonEnabled = true;
            mIsDisplayHomeAsUpEnabled = true;
            getSupportActionBar().setHomeButtonEnabled(mIsHomeButtonEnabled);
            getSupportActionBar().setDisplayHomeAsUpEnabled(mIsDisplayHomeAsUpEnabled);
        }

        LudoAudio audio = getIntent() != null && getIntent().getExtras() != null ? JsonUtil.getGson().fromJson(getIntent().getExtras().getString(KEY_AUDIO), LudoAudio.class) : new LudoAudio();
        String url = getIntent() != null && getIntent().getExtras() != null ? getIntent().getExtras().getString("VideoURL") : null;
        if (audio != null && nonEmptyNonNull(audio.getTitle())) {
            setTitle(getString(R.string.video_di_label, audio.getTitle()));
        } else {
            setTitle(getString(R.string.new_video_label));
        }

        boolean loadAtOnce;

        if (audio != null) {
            if (getIntent() != null && getIntent().getExtras() != null) {
                audio.getVideo().setAudioList(JsonUtil.getGson().fromJson((String) getIntent().getExtras().get(KEY_VIDEO_LIST), new TypeToken<List<LudoAudio>>() {
                }.getType()));
                loadAtOnce = getIntent().getExtras().getBoolean(KEY_LOAD_AT_ONCE);
            } else {
                loadAtOnce = false;
            }

            if (!loadAtOnce) {
                mPresenter.getVideoInformation(audio);
            } else {
                setVideo(this, audio.getVideo());
            }
        } else if (nonEmptyNonNull(url)) {
            mPresenter.getVideoByUrl(url);
        } else {
            throw new IllegalArgumentException("Fragment" + getClass().getName() + " called without url or audio.");
        }
    }

    @Override
    protected LinearLayout getAdRootView() {
        return (LinearLayout) mBinding.getRoot();
    }

    @Override
    protected void setContentView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.dialog_video);
    }

    @Override
    protected int getFragmentContainerView() {
        return 0;
    }

    private void setVideo(Context context, LudoVideo video) {
        VideoInformation videoInformation = video.getVideoInformation() != null ? video.getVideoInformation() : new VideoInformation();
        if (video.getThumbnail() != null && video.getThumbnail().getImage() != null) {
            mBinding.thumbnailImage.setImageBitmap(video.getThumbnail().getImage());
        } else {
            mPresenter.getThumbnail(video);
        }
        mBinding.titleLabel.setText(video.getTitle());

        mBinding.likes.setText(StringUtils.valueOf(videoInformation.getLikes()));
        mBinding.dislikes.setText(StringUtils.valueOf(videoInformation.getDislikes()));
        mBinding.viewsLabel.setText(StringUtils.valueOf(videoInformation.getViews()));
        mBinding.descrizioneLabel.setText(video.getDescription());
        if (!ListUtils.isEmptyOrNull(video.getConnectedAudioList())) {

            mBinding.audioListLabel.setVisibility(View.VISIBLE);

            for (LudoAudio audio : video.getConnectedAudioList()) {
                Button button = new Button(context);
                button.setText(audio.getTitle());
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                button.setLayoutParams(params);
                button.setOnClickListener(v -> AudioUtils.playTrack(context, audio));

                mBinding.audioListLayout.addView(button);
            }
        } else {
            mBinding.audioListLabel.setVisibility(View.GONE);
            mBinding.audioListLayout.setVisibility(View.GONE);
        }

        mBinding.thumbnailImage.setOnClickListener(v -> apriVideo(video));
    }

    public void apriVideo(LudoVideo item) {
        AdsActivity.AdClosedListener adClosedListener = () -> CommonUtils.openLink(this, buildVideoUrl(item.getId()));
        if (Math.random() < 0.5) {
            showInterstitialAd(adClosedListener);
        } else {
            adClosedListener.onAdClosed();
        }
    }

    @Override
    public void onThumbnailLoadSuccess(Thumbnail thumbnail) {
        mBinding.thumbnailImage.setImageBitmap(thumbnail.getImage());
    }

    @Override
    public void onThumbnailLoadFailed() {
        CommonUtils.showDialog(this, "Ooops! Non sono riuscito a caricare la thumbnail di questo video.");
    }

    @Override
    public void onVideoInformationLoadFailed() {
        CommonUtils.showDialog(this, "Ooops! Non sono riuscito a caricare le informazioni di questo video.", (dialog, which) -> finish(), false);

    }

    @Override
    public void onVideoInformationLoadSuccess(LudoAudio audio) {
        onVideoByUrlLoadSuccess(audio.getVideo());
    }

    @Override
    public void onVideoByUrlLoadSuccess(LudoVideo video) {
        setVideo(this, video);
    }
}
