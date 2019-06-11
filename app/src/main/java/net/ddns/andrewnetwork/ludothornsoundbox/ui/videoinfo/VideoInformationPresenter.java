package net.ddns.andrewnetwork.ludothornsoundbox.ui.videoinfo;

import android.content.SharedPreferences;

import net.ddns.andrewnetwork.ludothornsoundbox.data.DataManager;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.BasePresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.videoinfo.VideoInformationViewPresenterBinder.IVideoInformationPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.videoinfo.VideoInformationViewPresenterBinder.IVideoInformationView;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.PreferitiListAdapter;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.rx.SchedulerProvider;

import java.util.List;

import javax.inject.Inject;

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

    @Override
    public void getVideoInformation(LudoAudio audio) {
        getMvpView().showLoading();
        getCompositeDisposable().add(Single.fromObservable(getDataManager().getVideoById(audio))
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .subscribe(thumbnail -> {
                            getMvpView().onVideoInformationLoadSuccess(audio);
                            getMvpView().hideLoading();
                        }, throwable -> {
                            throwable.printStackTrace();
                            getMvpView().onVideoInformationLoadFailed();
                            getMvpView().hideLoading();
                        }
                ));
    }

    @Override
    public void getVideoInformation(LudoVideo video) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager().getVideoById(video.getId())
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .subscribe(video1 -> {
                            getMvpView().onVideoByUrlLoadSuccess(video1);
                            getMvpView().hideLoading();
                        }, throwable -> {
                            throwable.printStackTrace();
                            getMvpView().onVideoByUrlLoadSuccess(video);
                            getMvpView().hideLoading();
                        }
                ));
    }

    @Override
    public void getVideoByUrl(String url) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager().getVideoById(url)
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .subscribe(video -> {
                            getMvpView().onVideoByUrlLoadSuccess(video);
                            getMvpView().hideLoading();
                        }, throwable -> {
                            throwable.printStackTrace();
                            getMvpView().onVideoInformationLoadFailed();
                            getMvpView().hideLoading();
                        }
                ));
    }

    @Override
    public List<LudoVideo> getPreferitiList() {
        return getDataManager().getVideoPreferitiList();
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
    public boolean isPreferito(LudoVideo video) {
        List<LudoVideo> preferitiList = getPreferitiList();

        return preferitiList.contains(video);
    }

    @Override
    public void registerOnSharedPreferencesChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        getDataManager().registerOnSharedPreferencesChangeListener(onSharedPreferenceChangeListener);
    }

    @Override
    public void unregisterOnSharedPreferencesChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        getDataManager().unregisterOnSharedPreferencesChangeListener(onSharedPreferenceChangeListener);
    }

}
