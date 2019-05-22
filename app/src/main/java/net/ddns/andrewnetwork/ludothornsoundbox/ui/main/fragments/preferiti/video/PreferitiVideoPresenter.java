package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.video;

import android.util.Log;

import net.ddns.andrewnetwork.ludothornsoundbox.data.DataManager;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.BasePresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.PreferitiListAdapter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.video.PreferitiVideoViewPresenterBinder.IPreferitiView;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;

public class PreferitiVideoPresenter<V extends IPreferitiView> extends BasePresenter<V> implements PreferitiVideoViewPresenterBinder.IPreferitiPresenter<V> {

    @Inject
    public PreferitiVideoPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }


    @Override
    public List<LudoVideo> getPreferitiListFromPref() {
        return getDataManager().getVideoPreferitiList();
    }

    @Override
    public void rimuoviPreferito(LudoVideo video, PreferitiListAdapter.PreferitoDeletedListener<LudoVideo> preferitoDeletedListener) {
        List<LudoVideo> preferitiList = getPreferitiListFromPref() != null ? getPreferitiListFromPref() : new ArrayList<>();
        //CONTROLLA SE ESISTE GIA'
        if (!videoExists(preferitiList, video)) {
            getMvpView().onPreferitoNonEsistente(video);
            return;
        }

        //SE ESISTE, RIMUOVI.
        try {
            if (getDataManager().rimuoviVideoPreferito(video)) {
                getMvpView().onPreferitoRimossoSuccess();
                preferitoDeletedListener.onPreferitoDeleted(video);
            }
        } catch (Exception e) {
            e.printStackTrace();
            getMvpView().onPreferitoRimossoFailed(e.getCause().getMessage());
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
    public void saveInPref(LudoVideo video) {
        getDataManager().salvaVideoPreferito(video);
    }

    private boolean videoExists(List<LudoVideo> videoList, LudoVideo video) {
        for (LudoVideo videoInList : videoList) {
            if (videoInList.getId().equals(video.getId())) {
                return true;
            }
        }

        return false;
    }
}
