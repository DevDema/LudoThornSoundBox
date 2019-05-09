package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video;

import net.ddns.andrewnetwork.ludothornsoundbox.data.model.Channel;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.MvpPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.MvpView;

import java.util.Date;
import java.util.List;

public interface VideoViewPresenterBinder {

    interface IVideoView extends MvpView {

        void onVideoListLoadFailed();

        void onVideoListLoadSuccess(List<Channel> channelList);

        void onMoreVideoListLoadSuccess(List<LudoVideo> channelList);
    }

    interface IVideoPresenter<V extends IVideoView> extends MvpPresenter<V> {

        void getChannels(List<Channel> channelList);

        void getMoreVideos(List<Channel> channel, Date date, VideoFragment.MoreVideosLoadedListener moreVideosLoadedListener);

        void getMoreVideos(Channel channel, Date date, VideoFragment.MoreVideosLoadedListener moreVideosLoadedListener);
        //void getVideoList(List<Channel> channelList);
    }
}
