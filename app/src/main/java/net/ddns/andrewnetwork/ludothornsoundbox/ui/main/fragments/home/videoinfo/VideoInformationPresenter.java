package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.videoinfo;

import android.util.Log;

import net.ddns.andrewnetwork.ludothornsoundbox.data.DataManager;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.Channel;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.BasePresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.videoinfo.VideoInformationViewPresenterBinder.IVideoInformationPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.videoinfo.VideoInformationViewPresenterBinder.IVideoInformationView;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.VideoViewPresenterBinder.IVideoPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.VideoViewPresenterBinder.IVideoView;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.rx.SchedulerProvider;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.wrapper.GenericWrapper2;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

public class VideoInformationPresenter<V extends IVideoInformationView> extends BasePresenter<V> implements IVideoInformationPresenter<V> {

    @Inject
    public VideoInformationPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void getThumbnail(LudoVideo video) {
        getMvpView().showLoading();
        getCompositeDisposable().add(Single.fromObservable(getDataManager().getThumbnail(video))
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .subscribe(thumbnail -> {
                            getMvpView().onThumbnailLoadSuccess(thumbnail);
                            getMvpView().hideLoading();
                        }, throwable -> {
                            getMvpView().onThumbnailLoadFailed();
                            getMvpView().hideLoading();
                        }
                ));
    }


}
