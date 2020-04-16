package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video;

import android.util.Log;

import net.ddns.andrewnetwork.ludothornsoundbox.data.DataManager;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.Channel;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.BasePresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.PreferitiListAdapter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.VideoViewPresenterBinder.IVideoPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.VideoViewPresenterBinder.IVideoView;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.VideoUtils;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

import static net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.VideoFragment.ALL_CHANNELS;

public class VideoPresenter<V extends IVideoView> extends BasePresenter<V> implements IVideoPresenter<V> {

    @Inject
    public VideoPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void getChannels(List<Channel> channelList) {
        getCompositeDisposable().add(
                Observable.fromIterable(channelList)
                        .flatMap(channel -> getDataManager().getChannel(channel)
                                .flatMap(channelModified -> getDataManager().getVideoList(channel)
                                        .flatMap(videoList -> Observable.fromIterable(videoList)
                                                .flatMap(video -> getDataManager().getVideoInformation(video))
                                        )
                                )
                        )
                        .observeOn(getSchedulerProvider().ui())
                        .subscribeOn(getSchedulerProvider().io())
                        .subscribe(wrapper2 -> {
                        }, throwable -> {
                            Log.e("ChannelListREST", throwable.getMessage());

                            if (!isViewAttached()) {
                                return;
                            }
                            if (!getMvpView().isAppVisible()) {
                                return;
                            }

                            getMvpView().onVideoListLoadFailed();
                        }, () -> getMvpView().onVideoListLoadSuccess(channelList))
        );
    }

    @Override
    public void getMoreVideos(String searchString, Date date, VideoFragment.MoreVideosLoadedListener moreVideosLoadedListener, List<Channel> channelList) {
        getCompositeDisposable().add(getDataManager().searchMoreVideosAllChannels(searchString, date, channelList)
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .subscribe(videoList -> {
                            if (moreVideosLoadedListener != null) {
                                moreVideosLoadedListener.onMoreLoaded(videoList);
                            }

                            getMvpView().onSearchMoreVideosLoaded(videoList);
                        }, throwable -> {
                            Log.e("SearchVideosREST", throwable.getMessage());

                            if (!isViewAttached()) {
                                return;
                            }
                            if (!getMvpView().isAppVisible()) {
                                return;
                            }

                            getMvpView().onVideoListLoadFailed();
                        }
                )
        );
    }

    @Override
    public void searchVideos(String searchString, List<Channel> channelList) {
        getCompositeDisposable().add(getDataManager().searchVideosAllChannels(searchString, channelList)
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .subscribe(videoList -> getMvpView().onSearchVideosLoaded(videoList), throwable -> {
                            Log.e("SearchVideosREST", throwable.getMessage());

                            if (!isViewAttached()) {
                                return;
                            }
                            if (!getMvpView().isAppVisible()) {
                                return;
                            }

                            getMvpView().onVideoListLoadFailed();
                        }
                )
        );
    }

    @Override
    public void getMoreVideos(List<Channel> channelListInput, Date date, VideoFragment.MoreChannelsLoadedListener moreChannelsLoadedListener) {
        List<Channel> channelList = new ArrayList<>(channelListInput);
        Channel allChannel = VideoUtils.findChannelByName(channelList, ALL_CHANNELS);

        if (allChannel != null) {
            channelList.remove(allChannel);
        }

        getCompositeDisposable().add(
                Observable.fromIterable(channelList)
                        .flatMap(channel -> getDataManager().getMoreVideos(channel, date)
                                .flatMap(videoListResponse -> Observable.fromIterable(videoListResponse)
                                        .flatMap(video -> getDataManager().getVideoInformation(video)
                                        )
                                )
                        )
                        .observeOn(getSchedulerProvider().ui())
                        .subscribeOn(getSchedulerProvider().io())
                        .subscribe(wrapper2 -> {
                                }, throwable -> {
                                    Log.e("ChannelListREST", throwable.getMessage());

                                    if (!isViewAttached()) {
                                        return;
                                    }
                                    if (!getMvpView().isAppVisible()) {
                                        return;
                                    }
                                    getMvpView().onVideoListLoadFailed();
                                }, () -> {
                                    if (moreChannelsLoadedListener != null) {
                                        moreChannelsLoadedListener.onMoreLoaded(channelList);
                                    }

                                    getMvpView().onMoreVideoListLoadSuccess(channelList);
                                }
                        )
        );
    }

    @Override
    public void getMoreVideos(Channel channel, Date date, VideoFragment.MoreChannelsLoadedListener moreChannelsLoadedListener) {
        List<Channel> channels = new ArrayList<>();

        channels.add(channel);

        getMoreVideos(channels, date, moreChannelsLoadedListener);
    }

    @Override
    public void loadThumbnail(LudoVideo video, PreferitiListAdapter.ThumbnailLoadedListener thumbnailLoadedListener) {
        getCompositeDisposable().add(getDataManager().getThumbnail(video)
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .subscribe(thumbnailLoadedListener::onThumbnailLoaded, throwable -> {
                            Log.e("VideoThumbREST", video.getTitle() + " | " + throwable.getMessage());
                            thumbnailLoadedListener.onThumbnailLoaded(null);
                        }
                ));
    }


}
