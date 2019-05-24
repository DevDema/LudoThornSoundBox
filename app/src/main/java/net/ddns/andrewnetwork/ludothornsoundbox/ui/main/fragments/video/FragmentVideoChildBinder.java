package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video;

import net.ddns.andrewnetwork.ludothornsoundbox.data.model.Channel;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;

import java.util.Date;
import java.util.List;

public interface FragmentVideoChildBinder {

    interface FragmentVideoChild {

        void setRecyclerViewRefreshing(boolean refreshing);

        void onMoreVideosLoaded(List<Channel> videoList);

        void refreshPrefiti(List<LudoVideo> preferitiList);
    }

    interface FragmentVideoParent extends  FragmentAdapterVideoBinder {
        void refreshChannels(boolean usesGlobalLoading);

        void getMoreVideos(VideoFragment.MoreVideosLoadedListener moreVideosLoadedListener);

        void getMoreVideos(Channel channel, Date date, VideoFragment.MoreVideosLoadedListener moreVideosLoadedListener);
    }
}
