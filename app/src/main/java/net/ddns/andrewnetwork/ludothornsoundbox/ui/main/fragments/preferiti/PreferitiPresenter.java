package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti;

import android.util.Log;

import net.ddns.andrewnetwork.ludothornsoundbox.data.DataManager;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.BasePresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.PreferitiViewPresenterBinder.IPreferitiView;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

import static net.ddns.andrewnetwork.ludothornsoundbox.utils.StringUtils.nonEmptyNonNull;

public class PreferitiPresenter<V extends IPreferitiView> extends BasePresenter<V> implements PreferitiViewPresenterBinder.IPreferitiPresenter<V> {

    @Inject
    public PreferitiPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }


    @Override
    public List<LudoAudio> getPreferitiListFromPref() {
        return getDataManager().getPreferitiList();
    }

    @Override
    public void salvaAudio(LudoAudio audio) {
        getDataManager().saveAudio(audio);
    }

    @Override
    public void getPreferitiList() {
        List<LudoAudio> preferitiList = getDataManager().getPreferitiList();
        if (preferitiList != null && !preferitiList.isEmpty()) {
            getCompositeDisposable().add(Observable.fromIterable(preferitiList).flatMap(audio -> {
                        Observable<LudoAudio> doNothing = Observable.create(emitter -> emitter.onNext(audio));
                        if (audio.getVideo() != null) {
                            if (nonEmptyNonNull(audio.getVideo().getTitle())) {
                                return doNothing;
                            }
                            return getDataManager().getVideoById(audio);
                        }
                        return doNothing;
                    })
                            .toList()
                            .observeOn(getSchedulerProvider().ui())
                            .subscribeOn(getSchedulerProvider().io())
                            .subscribe(preferitiWithVideoList -> getMvpView().onPreferitiListLoaded(preferitiWithVideoList), throwable -> {
                                        Log.e("PreferitiListREST", throwable.getMessage());
                                        getMvpView().onPreferitiListError(preferitiList);
                                        getMvpView().hideLoading();
                                    }
                            )
            );
        } else {
            getMvpView().onPreferitiListEmpty();
        }
    }

    @Override
    public void rimuoviPreferito(LudoAudio audio) {
        List<LudoAudio> preferitiList = getDataManager().getPreferitiList() != null ? getDataManager().getPreferitiList() : new ArrayList<>();
        //CONTROLLA SE ESISTE GIA'
        if (!audioExists(preferitiList, audio)) {
            getMvpView().onPreferitoNonEsistente(audio);
            return;
        }

        //SE ESISTE, RIMUOVI.
        try {
            if (getDataManager().rimuoviPreferito(audio)) {
                getMvpView().onPreferitoRimossoSuccess();
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
    public void loadVideo(LudoAudio audio, PreferitiListAdapter.VideoLoadedListener videoLoadedListener) {
        getCompositeDisposable().add(getDataManager().getVideoById(audio)
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .subscribe(audioResponse -> videoLoadedListener.onVideoLoaded(audioResponse.getVideo()), throwable -> {
                            Log.e("VideoREST", audio.getTitle() + " | " + throwable.getMessage());
                            videoLoadedListener.onVideoLoaded(audio.getVideo());
                        }
                ));
    }

    @Override
    public void saveAudioInPref(LudoAudio audio) {
        getDataManager().salvaPreferito(audio);
    }

    private boolean audioExists(List<LudoAudio> audioList, LudoAudio audio) {
        for (LudoAudio audioInList : audioList) {
            if (audioInList.getAudio() == audio.getAudio()) {
                return true;
            }
        }

        return false;
    }
}
