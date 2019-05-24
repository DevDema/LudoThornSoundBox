package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.child;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.Channel;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.databinding.FragmentVideoListBinding;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.BaseFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.FragmentVideoChildBinder;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.FragmentVideoChildBinder.FragmentVideoParent;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.VideoFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.JsonUtil;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.VideoUtils;

import java.util.ArrayList;
import java.util.List;

import static net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.VideoFragment.ALL_CHANNELS;
import static net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.VideoFragment.isLoadingMoreVideos;
import static net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.VideoFragment.setLoadingMoreVideos;

public class ChildVideoFragment extends BaseFragment implements FragmentVideoChildBinder.FragmentVideoChild {

    private FragmentVideoParent parent;
    public static final String KEY_CHANNEL = "KEY_CHANNEL";
    private Channel channel;
    private FragmentVideoListBinding mBinding;

    public static ChildVideoFragment newInstance(Channel channel) {

        Bundle args = new Bundle();
        ChildVideoFragment fragment = new ChildVideoFragment();
        args.putString(KEY_CHANNEL, JsonUtil.getGson().toJson(channel));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.parent = (VideoFragment) getParentFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            channel = JsonUtil.getGson().fromJson(getArguments().getString(KEY_CHANNEL), Channel.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_video_list, container, false);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.masterLayout.setOnRefreshListener(() -> parent.refreshChannels(false));
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(new VideoRecyclerAdapter(mContext, getParent(), channel.getVideoList()));
        mBinding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    if (!isLoadingMoreVideos()) {
                        VideoFragment.MoreVideosLoadedListener moreVideosLoadedListener = null;
                        VideoRecyclerAdapter adapter = (VideoRecyclerAdapter) recyclerView.getAdapter();
                        if (adapter != null) {
                            adapter.showLoading();
                            moreVideosLoadedListener = videoList -> mActivity.runOnUiThread(adapter::hideLoading);
                        }

                        if(channel.getChannelName().equals(ALL_CHANNELS)) {
                            parent.getMoreVideos(moreVideosLoadedListener);
                        } else {
                            parent.getMoreVideos(channel, VideoUtils.getMostRecentDate(channel), moreVideosLoadedListener);
                        }

                        setLoadingMoreVideos(true);
                    }
                }
            }
        });
    }

    public FragmentVideoParent getParent() {
        return parent;
    }

    @Override
    public void setRecyclerViewRefreshing(boolean refreshing) {
        mBinding.masterLayout.setRefreshing(refreshing);
    }

    @Override
    public void onMoreVideosLoaded(List<Channel> videoList) {
        List<LudoVideo> videoChannelList = getVideosInChannel(videoList);
        if(mBinding.recyclerView.getAdapter() != null) {
            ((VideoRecyclerAdapter) mBinding.recyclerView.getAdapter()).addItems(videoChannelList);
        }

        mBinding.masterLayout.setRefreshing(false);
    }

    private List<LudoVideo> getVideosInChannel(List<Channel> channelList) {

        if(channel.getChannelName().equals(ALL_CHANNELS)) {
            return new ArrayList<>(VideoUtils.concatVideosInChannel(channelList));
        }

        return VideoUtils.getVideoInChannelByName(channelList, channel.getChannelName());
    }
}
