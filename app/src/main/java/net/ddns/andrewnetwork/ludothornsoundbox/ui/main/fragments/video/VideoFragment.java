package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import net.ddns.andrewnetwork.ludothornsoundbox.BuildConfig;
import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.Channel;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.databinding.ContentVideoBinding;
import net.ddns.andrewnetwork.ludothornsoundbox.di.component.ActivityComponent;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.BaseFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.VideoViewPresenterBinder.IVideoPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.VideoViewPresenterBinder.IVideoView;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.ColorUtils;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.CommonUtils;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.VideoUtils;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.view.StringParsingAdapter;

import java.util.ArrayList;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class VideoFragment extends BaseFragment implements IVideoView {

    private ContentVideoBinding mBinding;
    private boolean loadingMoreVideos;
    @Inject
    IVideoPresenter<IVideoView> mPresenter;
    private VideoRecyclerAdapter adapter;
    private boolean loadingFailed;

    interface MoreVideosLoadedListener {

        void onMoreVideosLoaded(List<LudoVideo> videoList);
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

        refreshChannels(true);

        return mBinding.getRoot();
    }

    @Override
    public void onVideoListLoadFailed() {
        mBinding.videoLayout.setRefreshing(false);
        CommonUtils.showDialog(mContext, "Oops! Sembra che si sia verificato un errore nel caricamento. \nContatta " + BuildConfig.SHORT_NAME + ", saprà sicuramente come risolvere il problema!");

        mBinding.progressBar.setVisibility(View.INVISIBLE);
        mBinding.progressVideoLoadingLabel.setVisibility(View.INVISIBLE);

        loadingFailed = true;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if(!hidden) {
            if(loadingFailed) {
                refreshChannels(true);
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View viewCreated, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(viewCreated, savedInstanceState);

        loadingFailed = false;
        mBinding.selectChannel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    adapter.getFilter().filter(null);
                    viewCreated.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorAccent));

                } else {
                    Channel channel = ((Channel) parent.getSelectedItem());
                    adapter.getFilter().filter(channel.getId());
                    viewCreated.setBackgroundColor(ContextCompat.getColor(mActivity, ColorUtils.getByName(mContext, channel.getBackGroundColor())));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mBinding.progressBar.getIndeterminateDrawable().setColorFilter(
                mContext.getResources().getColor(R.color.white),
                android.graphics.PorterDuff.Mode.SRC_IN);

        mBinding.videoLayout.setOnRefreshListener(() -> refreshChannels(false));

        mBinding.refreshButton.setOnClickListener(v -> refreshChannels(false));

        mBinding.progressVideoLoadingLabel.setText(mContext.getString(R.string.progress_video_loading_label, BuildConfig.SHORT_NAME));
    }

    @Override
    public void onVideoListLoadSuccess(List<Channel> channelList) {

        mBinding.videoHeader.setVisibility(View.VISIBLE);

        mBinding.videoLayout.setRefreshing(false);

        mBinding.videoRecycler.setLayoutManager(new LinearLayoutManager(mContext));

        mBinding.videoRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    if (!loadingMoreVideos) {
                        MoreVideosLoadedListener moreVideosLoadedListener = null;
                        VideoRecyclerAdapter adapter = (VideoRecyclerAdapter) mBinding.videoRecycler.getAdapter();
                        if (adapter != null) {
                            adapter.showLoading();
                            moreVideosLoadedListener = videoList -> mActivity.runOnUiThread(adapter::hideLoading);
                        }


                        if (mBinding.selectChannel.getSelectedItemPosition() == 0) {
                            mPresenter.getMoreVideos(channelList, VideoUtils.getMostRecentDate(channelList), moreVideosLoadedListener);
                        } else {
                            Channel channel = (Channel) mBinding.selectChannel.getSelectedItem();
                            mPresenter.getMoreVideos(channel, VideoUtils.getMostRecentDate(channel), moreVideosLoadedListener);
                        }
                        loadingMoreVideos = true;
                    }
                }
            }
        });
        List<Channel> selezionareChannel = new ArrayList<>(channelList);

        selezionareChannel.add(0, new Channel("Tutti i canali", null, ColorUtils.getByColorResource(mContext, R.color.colorAccent)));

        mBinding.selectChannel.setAdapter(new StringParsingAdapter<>(
                        Objects.requireNonNull(mContext),
                        R.layout.ludo_spinner_item,
                        selezionareChannel,
                        Channel::getChannelName
                )
        );

        adapter = new VideoRecyclerAdapter(mContext);

        mBinding.videoRecycler.setAdapter(adapter);

        mBinding.progressBar.setVisibility(View.INVISIBLE);
        mBinding.progressVideoLoadingLabel.setVisibility(View.INVISIBLE);

        onMoreVideoListLoadSuccess(VideoUtils.concatVideosInChannel(channelList));
    }

    @Override
    public void onMoreVideoListLoadSuccess(List<LudoVideo> videoList) {
        mActivity.runOnUiThread(() -> {
            adapter.addItems(videoList);
            loadingMoreVideos = false;
            mBinding.videoLayout.setRefreshing(false);
        });
    }

    private void refreshChannels(boolean usesGlobalLoading) {
        if(!usesGlobalLoading) {
            mBinding.videoLayout.setRefreshing(true);
        } else {
            mBinding.progressVideoLoadingLabel.setVisibility(View.VISIBLE);
            mBinding.progressBar.setVisibility(View.VISIBLE);
        }
        mPresenter.getChannels(VideoUtils.getChannels());
    }
}
