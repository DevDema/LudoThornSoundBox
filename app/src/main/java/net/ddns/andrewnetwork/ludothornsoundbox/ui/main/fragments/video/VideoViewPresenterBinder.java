package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video;

import android.content.SharedPreferences;

import net.ddns.andrewnetwork.ludothornsoundbox.data.model.Channel;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.MvpPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.MvpView;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.PreferitiListAdapter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.PreferitiListAdapter.ThumbnailLoadedListener;

import java.util.Date;
import java.util.List;

public interface VideoViewPresenterBinder {

    interface IVideoView extends MvpView {

        void onVideoListLoadFailed();

        void onVideoListLoadSuccess(List<Channel> channelList);

        void onMoreVideoListLoadSuccess(List<Channel> channelList);
    }

    interface IVideoPresenter<V extends IVideoView> extends MvpPresenter<V> {

        void getChannels(List<Channel> channelList);

        void getMoreVideos(List<Channel> channel, Date date, VideoFragment.MoreVideosLoadedListener moreVideosLoadedListener);

        void getMoreVideos(Channel channel, Date date, VideoFragment.MoreVideosLoadedListener moreVideosLoadedListener);

        void loadThumbnail(LudoVideo item, ThumbnailLoadedListener thumbnailLoadedListener);

    }
}
