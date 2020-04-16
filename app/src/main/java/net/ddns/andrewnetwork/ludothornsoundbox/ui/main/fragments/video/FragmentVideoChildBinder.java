package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video;

import net.ddns.andrewnetwork.ludothornsoundbox.data.model.Channel;

import java.util.Date;
import java.util.List;

public interface FragmentVideoChildBinder {

    interface FragmentVideoChild {

        void setRecyclerViewRefreshing(boolean refreshing);

        void onMoreVideosLoaded(List<Channel> videoList);
    }

    interface FragmentVideoParent extends  FragmentAdapterVideoBinder {
        void refresh(boolean usesGlobalLoading);

        void getMoreVideos(VideoFragment.MoreChannelsLoadedListener moreChannelsLoadedListener);

        void getMoreVideos(Channel channel, Date date, VideoFragment.MoreChannelsLoadedListener moreChannelsLoadedListener);

        void getMoreVideos(String searchString, Date date, VideoFragment.MoreVideosLoadedListener moreChannelsLoadedListener);

        void refreshSearch(String text);
    }
}
