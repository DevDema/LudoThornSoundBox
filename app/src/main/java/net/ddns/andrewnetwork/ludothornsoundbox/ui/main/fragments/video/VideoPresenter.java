package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video;

import android.content.SharedPreferences;
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
    public void getMoreVideos(List<Channel> channelListInput, Date date, VideoFragment.MoreVideosLoadedListener moreVideosLoadedListener) {
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
                                    if (moreVideosLoadedListener != null) {
                                        moreVideosLoadedListener.onMoreVideosLoaded(channelList);
                                    }

                                    getMvpView().onMoreVideoListLoadSuccess(channelList);
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
    public void aggiungiPreferito(LudoVideo video, PreferitiListAdapter.PreferitoDeletedListener<LudoVideo> preferitoDeletedListener) {
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

        if (preferitoDeletedListener != null) {
            preferitoDeletedListener.onPreferitoDeleted(video);
        }

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

    @Override
    public void rimuoviPreferito(LudoVideo item, PreferitiListAdapter.PreferitoDeletedListener<LudoVideo> preferitoDeletedListener) {
        if (getDataManager().rimuoviVideoPreferito(item)) {
            getMvpView().onPreferitoRimossoSuccess(item);
            if (preferitoDeletedListener != null) {
                preferitoDeletedListener.onPreferitoDeleted(item);
            }
        } else {
            getMvpView().onPreferitoRimossoFailed();
        }
    }

    @Override
    public void registerOnSharedPreferencesChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        getDataManager().registerOnSharedPreferencesChangeListener(onSharedPreferenceChangeListener);
    }

    @Override
    public void unregisterOnSharedPreferencesChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        getDataManager().unregisterOnSharedPreferencesChangeListener(onSharedPreferenceChangeListener);
    }

    @Override
    public List<LudoVideo> getPreferitiList() {
        return getDataManager().getVideoPreferitiList();
    }
}
