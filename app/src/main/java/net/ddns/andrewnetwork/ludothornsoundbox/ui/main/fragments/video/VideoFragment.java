package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.ddns.andrewnetwork.ludothornsoundbox.BuildConfig;
import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.Channel;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.databinding.ContentVideoBinding;
import net.ddns.andrewnetwork.ludothornsoundbox.di.component.ActivityComponent;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.MainActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.AdsActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.MainFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.PreferitiListAdapter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.FragmentVideoChildBinder.FragmentVideoChild;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.VideoViewPresenterBinder.IVideoPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.VideoViewPresenterBinder.IVideoView;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.AudioUtils;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.ColorUtils;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.CommonUtils;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.VideoUtils;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import static net.ddns.andrewnetwork.ludothornsoundbox.data.prefs.AppPreferencesHelper.PREF_KEY_PREFERITI_VIDEO;
import static net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.controller.VideoManager.buildVideoUrl;

public class VideoFragment extends MainFragment implements IVideoView, FragmentAdapterVideoBinder, FragmentVideoChildBinder.FragmentVideoParent, SharedPreferences.OnSharedPreferenceChangeListener {

    private ContentVideoBinding mBinding;
    private static boolean loadingMoreVideos;
    @Inject
    IVideoPresenter<IVideoView> mPresenter;
    private boolean loadingFailed;
    public static final String ALL_CHANNELS = "Tutti";
    private List<Channel> channelList;
    private int currentPosition = -1;
    private TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            currentPosition = tab.getPosition();
            Channel channel = channelList.get(tab.getPosition());

            if(getView() != null) {
                getView().setBackgroundColor(ContextCompat.getColor(mActivity, ColorUtils.getByName(mContext, channel.getBackGroundColor())));
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };
    public interface MoreVideosLoadedListener {

        void onMoreVideosLoaded(List<Channel> videoList);
    }

    public static VideoFragment newInstance() {

        Bundle args = new Bundle();

        VideoFragment fragment = new VideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mBinding = DataBindingUtil.inflate(inflater, R.layout.content_video, container, false);

        ActivityComponent activityComponent = getActivityComponent();
        if (activityComponent != null) {
            activityComponent.inject(this);
            mPresenter.onAttach(this);
        }

        mPresenter.registerOnSharedPreferencesChangeListener(this);

        refreshChannels(true);

        return mBinding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mPresenter.unregisterOnSharedPreferencesChangeListener(this);
    }

    @Override
    public void onVideoListLoadFailed() {
        //mBinding.videoLayout.setRefreshing(false);
        CommonUtils.showDialog(mContext, "Oops! Sembra che si sia verificato un errore nel caricamento. \nContatta " + BuildConfig.SHORT_NAME + ", saprà sicuramente come risolvere il problema!");

        mBinding.progressBar.setVisibility(View.INVISIBLE);
        mBinding.progressVideoLoadingLabel.setVisibility(View.INVISIBLE);

        loadingFailed = true;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            if (loadingFailed) {
                refreshChannels(true);
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View viewCreated, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(viewCreated, savedInstanceState);

        loadingFailed = false;

        mBinding.progressBar.getIndeterminateDrawable().setColorFilter(
                mContext.getResources().getColor(R.color.white),
                android.graphics.PorterDuff.Mode.SRC_IN);

        mBinding.progressVideoLoadingLabel.setText(mContext.getString(R.string.progress_video_loading_label, BuildConfig.SHORT_NAME));
    }

    @Override
    public void onVideoListLoadSuccess(List<Channel> channelList) {

        this.channelList = channelList;

        if (channelList.size() > 1) {
            channelList.add(0, new Channel(ALL_CHANNELS, null, ColorUtils.getByColorResource(mContext, R.color.colorAccent)));
        }

        notifyFragmentsRefreshing(false);

        setUpTabLayout();
        mBinding.progressBar.setVisibility(View.INVISIBLE);
        mBinding.progressVideoLoadingLabel.setVisibility(View.INVISIBLE);
        onMoreVideoListLoadSuccess(channelList);

        if (currentPosition != -1) {
            TabLayout.Tab tab = mBinding.tabLayout.getTabAt(currentPosition);
            if (tab != null) {
                tab.select();
            }
        }

        mBinding.tabLayout.addOnTabSelectedListener(onTabSelectedListener);
    }

    private void setUpTabLayout() {


        mBinding.tabLayout.removeAllTabs();

        for (Channel channel : channelList) {
            TabLayout.Tab tab = mBinding.tabLayout.newTab();
            mBinding.tabLayout.addTab(tab.setText(channel.getChannelName()));
        }

        mBinding.viewPager.setAdapter(new VideoPagerAdapter(getChildFragmentManager(), channelList, mPresenter.getPreferitiList()));
        mBinding.viewPager.setOffscreenPageLimit(channelList.size());
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(PREF_KEY_PREFERITI_VIDEO)) {
            notifyRefreshPreferiti(mPresenter.getPreferitiList());
        }
    }

    @Override
    public void onMoreVideoListLoadSuccess(List<Channel> videoList) {
        mActivity.runOnUiThread(() -> {
            notifyMoreVideosLoaded(videoList);
            VideoFragment.loadingMoreVideos = false;
        });
    }

    private void notifyRefreshPreferiti(List<LudoVideo> preferitiList) {
        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            if (fragment instanceof FragmentVideoChild) {
                ((FragmentVideoChild) fragment).refreshPrefiti(preferitiList);
            }
        }
    }

    private void notifyMoreVideosLoaded(List<Channel> videoList) {
        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            if (fragment instanceof FragmentVideoChild) {
                ((FragmentVideoChild) fragment).onMoreVideosLoaded(videoList);
            }
        }
    }

    @Override
    public void onPreferitoSavedSuccess(LudoVideo video) {
        if (getView() != null) {
            Snackbar snackbar = Snackbar.make(getView(), mContext.getString(R.string.video_aggiunto_preferiti), Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }

    @Override
    public void onMaxVideoReached() {
        CommonUtils.showDialog(mActivity, mActivity.getString(R.string.max_video_reached_label));
    }

    @Override
    public void onPreferitoEsistente(LudoVideo video) {
        CommonUtils.showDialog(mActivity, getString(R.string.video_esistente_label));
    }

    @Override
    public void onPreferitoRimossoSuccess(LudoVideo item) {
        if (getView() != null) {
            Snackbar snackbar = Snackbar.make(getView(), mContext.getString(R.string.video_rimosso_preferiti_label), Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }

    @Override
    public void onPreferitoRimossoFailed() {
        Toast.makeText(mContext, R.string.generic_error_label, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void refreshChannels(boolean usesGlobalLoading) {
        mBinding.tabLayout.removeOnTabSelectedListener(onTabSelectedListener);

        if (!usesGlobalLoading) {
            notifyFragmentsRefreshing(true);
        } else {
            mBinding.progressVideoLoadingLabel.setVisibility(View.VISIBLE);
            mBinding.progressBar.setVisibility(View.VISIBLE);
        }
        mPresenter.getChannels(VideoUtils.getChannels());
    }

    private void notifyFragmentsRefreshing(boolean refreshing) {
        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            if (fragment instanceof FragmentVideoChild) {
                ((FragmentVideoChild) fragment).setRecyclerViewRefreshing(refreshing);
            }
        }
    }

    @Override
    public void aggiungiPreferito(LudoVideo video, PreferitiListAdapter.PreferitoDeletedListener<LudoVideo> preferitoDeletedListener) {
        mPresenter.aggiungiPreferito(video, preferitoDeletedListener);
    }

    @Override
    public void loadThumbnail(LudoVideo item, PreferitiListAdapter.ThumbnailLoadedListener thumbnailLoadedListener) {
        mPresenter.loadThumbnail(item, thumbnailLoadedListener);
    }

    @Override
    public void rimuoviPreferito(LudoVideo item, PreferitiListAdapter.PreferitoDeletedListener<LudoVideo> preferitoDeletedListener) {
        mPresenter.rimuoviPreferito(item, preferitoDeletedListener);
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

    public static boolean isLoadingMoreVideos() {
        return loadingMoreVideos;
    }

    public static void setLoadingMoreVideos(boolean loadingMoreVideos) {
        VideoFragment.loadingMoreVideos = loadingMoreVideos;
    }

    public void getMoreVideos(Channel channel, Date mostRecentDate, MoreVideosLoadedListener moreVideosLoadedListener) {
        mPresenter.getMoreVideos(channel, mostRecentDate, moreVideosLoadedListener);
    }

    public void getMoreVideos(MoreVideosLoadedListener moreVideosLoadedListener) {

        mPresenter.getMoreVideos(channelList, VideoUtils.getMostRecentDate(channelList), moreVideosLoadedListener);
    }
}
