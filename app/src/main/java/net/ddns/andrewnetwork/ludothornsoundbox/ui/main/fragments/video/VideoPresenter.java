package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video;

import android.util.Log;

import net.ddns.andrewnetwork.ludothornsoundbox.data.DataManager;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.Channel;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.BasePresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.VideoViewPresenterBinder.IVideoPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.VideoViewPresenterBinder.IVideoView;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.rx.SchedulerProvider;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.wrapper.GenericWrapper2;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

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
                            getMvpView().onVideoListLoadFailed();
                        }, () -> getMvpView().onVideoListLoadSuccess(channelList))
        );
    }

    @Override
    public void getMoreVideos(List<Channel> channelList, Date date, VideoFragment.MoreVideosLoadedListener moreVideosLoadedListener) {
        getCompositeDisposable().add(
                Observable.fromIterable(channelList)
                        .flatMap(channel -> getDataManager().getMoreVideos(channel, date)
                                .flatMap(videoListResponse -> Observable.fromIterable(videoListResponse)
                                        .flatMap(video ->, getDataManager().getVideoInformation(video),
                                                GenericWrapper2::new
                                                )
                                        ).doOnComplete(() -> {
                                            if (moreVideosLoadedListener != null) {
                                                moreVideosLoadedListener.onMoreVideosLoaded(videoListResponse);
                                            }

                                            getMvpView().onMoreVideoListLoadSuccess(videoListResponse);
                                        })
                                )
                        )
                        .observeOn(getSchedulerProvider().ui())
                        .subscribeOn(getSchedulerProvider().io())
                        .subscribe(wrapper2 -> {
                                }, throwable -> {
                                    Log.e("ChannelListREST", throwable.getMessage());
                                    getMvpView().onVideoListLoadFailed();
                                }, () -> {
                                }
                        )
        );
    }

    @Override
    public void getMoreVideos(Channel channel, Date date, VideoFragment.MoreVideosLoadedListener moreVideosLoadedListener) {
        List<Channel> channels = new ArrayList<>();

        channels.add(channel);

        getMoreVideos(channels, date, moreVideosLoadedListener);
    }

    @Override
    public void aggiungiPreferito(LudoVideo video) {
        List<LudoVideo> preferitiList = getPreferitiList();

        //CONTROLLA SE HAI RAGGIUNTO IL NUMERO MASSIMO DI PREFERITI.
        if (preferitiList.size() >= 5) {
            getMvpView().onMaxVideoReached();
            return;
        }

        //CONTROLLA SE ESISTE GIA'
        for (LudoVideo videoInList : preferitiList) {
            if (videoInList.getId().equals(video.getId())) {
                getMvpView().onPreferitoEsistente(video);
                return;
            }
        }

        getDataManager().salvaVideoPreferito(video);
        getMvpView().onPreferitoSavedSuccess(video);
    }

    @Override
    public List<LudoVideo> getPreferitiList() {
        return getDataManager().getVideoPreferitiList();
    }
}
