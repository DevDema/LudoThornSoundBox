package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.Channel;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.databinding.ContentVideoBinding;
import net.ddns.andrewnetwork.ludothornsoundbox.di.component.ActivityComponent;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.ParentFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.VideoViewPresenterBinder.IVideoPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.VideoViewPresenterBinder.IVideoView;
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

public class VideoFragment extends ParentFragment implements IVideoView {

    private ContentVideoBinding mBinding;
    private boolean loadingMoreVideos;
    @Inject
    IVideoPresenter<IVideoView> mPresenter;
    private VideoRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mBinding = DataBindingUtil.inflate(inflater, R.layout.content_video, container, false);

        ActivityComponent activityComponent = getActivityComponent();
        if (activityComponent != null) {
            activityComponent.inject(this);
            mPresenter.onAttach(this);
        }

        refreshChannels();

        return mBinding.getRoot();
    }

    @Override
    public void onVideoListLoadFailed() {
        CommonUtils.showDialog(getContext(), "Oops! Sembra che si sia verificato un errore nel caricamento. \nContatta Ludo, saprà sicuramente come risolvere il problema!");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.selectChannel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    adapter.getFilter().filter(null);
                } else {
                    Channel channel = ((Channel) parent.getSelectedItem());
                    adapter.getFilter().filter(channel.getId());
                    if(getView() != null && getContext() != null) {
                        getView().setBackgroundColor(ContextCompat.getColor(getContext(), channel.getBackGroundColor()));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mBinding.videoLayout.setOnRefreshListener(this::refreshChannels);


        mBinding.refreshButton.setOnClickListener(v -> refreshChannels());
    }

    @Override
    public void onVideoListLoadSuccess(List<Channel> channelList) {


        mBinding.videoRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        mBinding.videoRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    if(!loadingMoreVideos) {
                        if (mBinding.selectChannel.getSelectedItemPosition() == 0) {
                            mPresenter.getMoreVideos(channelList, VideoUtils.getMostRecentDate(channelList));
                        } else {
                            Channel channel = (Channel) mBinding.selectChannel.getSelectedItem();
                            mPresenter.getMoreVideos(channel, VideoUtils.getMostRecentDate(channel));
                        }
                        loadingMoreVideos = true;
                    }
                }
            }
        });
        List<Channel> selezionareChannel = new ArrayList<>(channelList);

        selezionareChannel.add(0, new Channel("Tutti i canali", null, R.color.background));


        mBinding.selectChannel.setAdapter(new StringParsingAdapter<>(
                        Objects.requireNonNull(getContext()),
                        R.layout.ludo_spinner_item,
                        selezionareChannel,
                        Channel::getChannelName
                )
        );

        adapter = new VideoRecyclerAdapter(this);

        mBinding.videoRecycler.setAdapter(adapter);

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

    private void refreshChannels() {
        mPresenter.getChannels(VideoUtils.getChannels());
    }
}
