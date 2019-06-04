package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.audio;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.DataSingleTon;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.databinding.ContentAudioFavoriteBinding;
import net.ddns.andrewnetwork.ludothornsoundbox.di.component.ActivityComponent;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.MainActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.AdsActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.ChildPreferitiFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.PreferitiListAdapter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.audio.PreferitiAudioViewPresenterBinder.IPreferitiPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.audio.PreferitiAudioViewPresenterBinder.IPreferitiView;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.AudioUtils;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.CommonUtils;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.google.android.material.snackbar.Snackbar;

import static net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.controller.VideoManager.buildVideoUrl;

public class PreferitiAudioFragment extends ChildPreferitiFragment implements IPreferitiView, IFragmentAudioPreferitiAdapterBinder {

    private final static int TIMER_DURATION = 5000;

    private ContentAudioFavoriteBinding mBinding;
    @Inject
    IPreferitiPresenter<IPreferitiView> mPresenter;
    private Snackbar snackbar;
    private CountDownTimer timer;

    public static PreferitiAudioFragment newInstance() {

        Bundle args = new Bundle();

        PreferitiAudioFragment fragment = new PreferitiAudioFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.content_audio_favorite, container, false);

        ActivityComponent activityComponent = getActivityComponent();

        if (activityComponent != null) {
            activityComponent.inject(this);
            mPresenter.onAttach(this);
        }

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadPreferiti();
    }

    private void loadPreferiti() {
        List<LudoAudio> audioList = mPresenter.getPreferitiListFromPref();
        if (audioList.isEmpty()) {
            onPreferitiListEmpty();
        } else {
            onPreferitiListLoaded(audioList);
        }
    }

    @Override
    public void onParentHiddenChanged(boolean hidden) {
        super.onParentHiddenChanged(hidden);
    }

    @Override
    protected PreferitiListAdapter getListAdapter() {
        return (PreferitiListAdapter) mBinding.recyclerView.getAdapter();
    }

    @Override
    public void onPreferenceChanged(SharedPreferences sharedPreferences) {
        loadPreferiti();
    }

    @Override
    public void onPreferitoNonEsistente(LudoAudio audio) {
        CommonUtils.showDialog(getContext(), getString(R.string.audio_non_rimosso_preferiti_label));

    }

    @Override
    public void onPreferitoRimossoSuccess() {
        if (getView() != null) {
            snackbar = Snackbar.make(getView(), mContext.getString(R.string.audio_rimosso_preferiti_label), Snackbar.LENGTH_SHORT);
            snackbar.show();
        }

        loadPreferiti();
    }

    @Override
    public void onPreferitoRimossoFailed(String message) {

        if (getView() != null) {
            snackbar = Snackbar.make(getView(), mContext.getString(R.string.audio_non_rimosso_preferiti_label), Snackbar.LENGTH_SHORT);
            snackbar.show();
        }

    }

    @Override
    public void onPreferitiListLoaded(List<LudoAudio> audioList) {

        mBinding.globalLinear.setVisibility(View.GONE);
        mBinding.recyclerView.setVisibility(View.VISIBLE);

        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        if (mBinding.recyclerView.getItemAnimator() != null) {
            ((SimpleItemAnimator) mBinding.recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        }

        PreferitiAudioListAdapter currentAdapter = (PreferitiAudioListAdapter) mBinding.recyclerView.getAdapter();

        mBinding.recyclerView.setAdapter(new PreferitiAudioListAdapter(this, getParent(), mContext, audioList));
    }

    @Override
    public void onPreferitiListError(List<LudoAudio> audioListWithoutThumbnail) {

        CommonUtils.showDialog(mContext, "Impossibile caricare le informazioni dei video.");
        onPreferitiListLoaded(audioListWithoutThumbnail);
    }

    @Override
    public void onPreferitiListEmpty() {
        mBinding.globalLinear.setVisibility(View.VISIBLE);
        mBinding.recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void playAudio(LudoAudio audio) {
        mBinding.audioPlayer.play(audio);
    }

    @Override
    public void stopAudio(LudoAudio audio) {
        mBinding.audioPlayer.stop();
    }

    @Override
    public void loadThumbnail(LudoAudio audio, PreferitiListAdapter.ThumbnailLoadedListener thumbnailLoadedListener) {
        LudoVideo video = audio.getVideo();
        if (video != null) {
            mPresenter.loadThumbnail(video, thumbnailLoadedListener);
        } else {
            thumbnailLoadedListener.onThumbnailLoaded(null);
        }
    }

    @Override
    public void setOnCompletionListener(MediaPlayer.OnCompletionListener onCompletionListener) {
        mBinding.audioPlayer.setOnCompletionListener(onCompletionListener);
    }

    @Override
    public void loadVideo(LudoAudio audio, PreferitiAudioListAdapter.VideoLoadedListener videoLoadedListener) {
        mPresenter.loadVideo(audio, videoLoadedListener);
    }

    @Override
    public void apriVideo(LudoVideo item) {
        AdsActivity.AdClosedListener adClosedListener = () -> {
            AudioUtils.stopTrack();
            CommonUtils.openLink(mContext, buildVideoUrl(item.getId()));
        };
        if (Math.random() < 0.5) {
            ((MainActivity) mActivity).showInterstitialAd(adClosedListener);
        } else {
            adClosedListener.onAdClosed();
        }
    }

    @Override
    public void saveInPref(LudoAudio audio) {
        mPresenter.saveInPref(audio);
    }

    @Override
    public void onPreferitoIntentDelete(LudoAudio audio, PreferitiAudioListAdapter.PreferitoDeletedListener<LudoAudio> preferitoDeletedListener) {
        if (getView() != null) {
            snackbar = Snackbar.make(getView(), mContext.getString(R.string.countdown_delete_favorite_message, 5), Snackbar.LENGTH_INDEFINITE);

            snackbar.show();

            timer = new CountDownTimer(TIMER_DURATION, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    snackbar.setText(mContext.getString(R.string.countdown_delete_favorite_message, millisUntilFinished / 1000));
                }

                @Override
                public void onFinish() {
                    snackbar.dismiss();
                    mPresenter.rimuoviPreferito(audio, preferitoDeletedListener);
                }

            };

            timer.start();

        }
    }

    @Override
    public void cancelPreferitoIntentDelete() {
        if (timer != null) {
            timer.cancel();

            if (getView() != null) {
                snackbar = Snackbar.make(getView(), mContext.getString(R.string.canceled_operation_message), Snackbar.LENGTH_SHORT);

                snackbar.show();
            }
        }
    }
}
