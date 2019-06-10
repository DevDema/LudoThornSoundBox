package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.video;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.databinding.ContentVideoFavoriteBinding;
import net.ddns.andrewnetwork.ludothornsoundbox.di.component.ActivityComponent;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.MainActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.AdsActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.ChildPreferitiFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.PreferitiListAdapter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.video.PreferitiVideoViewPresenterBinder.IPreferitiPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.video.PreferitiVideoViewPresenterBinder.IPreferitiView;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.videoinfo.VideoInformationActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.AudioUtils;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.CommonUtils;

import java.util.List;

import javax.inject.Inject;

import static net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.controller.VideoManager.buildVideoUrl;

public class PreferitiVideoFragment extends ChildPreferitiFragment implements IPreferitiView, IFragmentVideoPreferitiAdapterBinder {

    private final static int TIMER_DURATION = 5000;
    private ContentVideoFavoriteBinding mBinding;
    @Inject
    IPreferitiPresenter<IPreferitiView> mPresenter;
    private Snackbar snackbar;
    private CountDownTimer timer;

    public static PreferitiVideoFragment newInstance() {

        Bundle args = new Bundle();

        PreferitiVideoFragment fragment = new PreferitiVideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.content_video_favorite, container, false);

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

        mBinding.swipeRefreshLayout.setOnRefreshListener(this::loadPreferiti);
    }

    @Override
    public void onPreferitoNonEsistente(LudoVideo video) {
        CommonUtils.showDialog(getContext(), getString(R.string.audio_non_rimosso_preferiti_label));
    }

    @Override
    public void onPreferitoRimossoSuccess() {
        if (getView() != null) {
            snackbar = Snackbar.make(getView(), mContext.getString(R.string.video_rimosso_preferiti_label), Snackbar.LENGTH_SHORT);
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
    public void onPreferitiListLoaded(List<LudoVideo> videoList) {
        PreferitiVideoListAdapter adapter = new PreferitiVideoListAdapter(this, getParent(), mContext, videoList);

        mBinding.videoRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.videoRecycler.setAdapter(adapter);

        mBinding.globalLinear.setVisibility(View.GONE);
        mBinding.videoRecycler.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPreferitiListError(List<LudoVideo> videoList) {
        CommonUtils.showDialog(mContext, "Impossibile caricare le informazioni dei video.");
        onPreferitiListLoaded(videoList);
    }

    @Override
    public void apriVideo(LudoVideo item) {
        AdsActivity.AdClosedListener adClosedListener = () -> VideoInformationActivity.newInstance(mContext, item);

        if (Math.random() < 0.5) {
            ((MainActivity) mActivity).showInterstitialAd(adClosedListener);
        } else {
            adClosedListener.onAdClosed();
        }
    }

    @Override
    public void onPreferitiListEmpty() {
        mBinding.globalLinear.setVisibility(View.VISIBLE);
        mBinding.videoRecycler.setVisibility(View.GONE);
    }

    @Override
    public void onPreferenceChanged(SharedPreferences sharedPreferences) {
        loadPreferiti();
    }


    @Override
    public void loadThumbnail(LudoVideo item, PreferitiListAdapter.ThumbnailLoadedListener thumbnailLoadedListener) {
        mPresenter.loadThumbnail(item, thumbnailLoadedListener);
    }

    @Override
    public void saveInPref(LudoVideo item) {
        mPresenter.saveInPref(item);
    }

    @Override
    public void onPreferitoIntentDelete(LudoVideo video, PreferitiListAdapter.PreferitoDeletedListener<LudoVideo> preferitoDeletedListener) {
        if (getView() != null) {
            snackbar = Snackbar.make(getView(), mContext.getString(R.string.countdown_delete_favorite_video_message, 5), Snackbar.LENGTH_INDEFINITE);

            snackbar.show();

            timer = new CountDownTimer(TIMER_DURATION, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    snackbar.setText(mContext.getString(R.string.countdown_delete_favorite_video_message, millisUntilFinished / 1000));
                }

                @Override
                public void onFinish() {
                    snackbar.dismiss();
                    mPresenter.rimuoviPreferito(video, preferitoDeletedListener);
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

    private void loadPreferiti() {
        List<LudoVideo> audioList = mPresenter.getPreferitiListFromPref();
        if (audioList.isEmpty()) {
            onPreferitiListEmpty();
        } else {
            onPreferitiListLoaded(audioList);
        }

        mBinding.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onParentHiddenChanged(boolean hidden) {
        super.onParentHiddenChanged(hidden);
    }

    @Override
    protected PreferitiListAdapter getListAdapter() {
        return (PreferitiListAdapter) mBinding.videoRecycler.getAdapter();
    }


    @Override
    public void loadChannel(LudoVideo video, OnChannelLoadedListener onChannelLoadedListener) {
        mPresenter.loadChannel(video, onChannelLoadedListener);
    }
}
