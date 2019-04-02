package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home;

import android.util.Log;

import net.ddns.andrewnetwork.ludothornsoundbox.data.DataManager;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.BasePresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.HomeViewPresenterBinder.IHomePresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.HomeViewPresenterBinder.IHomeView;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.rx.SchedulerProvider;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.wrapper.GenericWrapper2;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongUnaryOperator;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

import static net.ddns.andrewnetwork.ludothornsoundbox.utils.StringUtils.nonEmptyNonNull;

public class HomePresenter<V extends IHomeView> extends BasePresenter<V> implements IHomePresenter<V> {

    @Inject
    public HomePresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }


    @Override
    public void salvaPreferito(LudoAudio audio) {
        List<LudoAudio> preferitiList = getDataManager().getPreferitiList() != null ? getDataManager().getPreferitiList() : new ArrayList<>();

        //CONTROLLA SE HAI RAGGIUNTO IL NUMERO MASSIMO DI PREFERITI.
        if(preferitiList.size() >= 5) {
            getMvpView().onMaxAudioReached();
            return;
        }

        //CONTROLLA SE ESISTE GIA'
        for (LudoAudio audioInList : preferitiList) {
            if (audioInList.getAudio() == audio.getAudio()) {
                getMvpView().onPreferitoEsistente(audio);
                return;
            }
        }

        //SE NON ESISTE, AGGIUNGI.
        try {

            if (getDataManager().salvaPreferito(audio)) {
                getMvpView().onPreferitoSalvataggioSuccess();
            }
        } catch (Exception e) {
            e.printStackTrace();
            getMvpView().onPreferitoSalvataggioFailed(e.getCause().getMessage());
        }
    }

    @Override
    public void getVideoInformationForAudios(List<LudoAudio> audioList) {
        Single<List<LudoAudio>> getAudioListFromPref = Single.create(emitter -> emitter.onSuccess(getDataManager().getAudioSavedList()));
        getMvpView().showLoading();
        getCompositeDisposable().add(Single.zip(Observable.fromIterable(audioList).flatMap(audio -> getDataManager().getVideoById(audio)).toList(),
                getAudioListFromPref,
                GenericWrapper2::new)
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .subscribe(audioListWrapper -> {
                    getMvpView().onAudioListReceived(manageAudioLists(audioListWrapper));
                    getMvpView().hideLoading();
                }, throwable -> {
                    throwable.printStackTrace();
                    getMvpView().onVideoInformationNotLoaded();
                    getMvpView().hideLoading();
                })
        );
    }

    @Override
    public void salvaAudio(LudoAudio audio) {
        getDataManager().saveAudio(audio);
    }

    private List<LudoAudio> manageAudioLists(GenericWrapper2<List<LudoAudio>, List<LudoAudio>> listListGenericWrapper2) {
        List<LudoAudio> listFromServer = listListGenericWrapper2.getFirstSource();
        List<LudoAudio> listFromPref = listListGenericWrapper2.getSecondSource();
        boolean found = false;
        for(LudoAudio audioFromServer : listFromServer) {
            for(LudoAudio audioFromPref : listFromPref) {
                if(audioFromServer.getTitle().equals(audioFromPref.getTitle())) {

                    //SE AUDIO HANNO RESOURCE ID DIFFERENTI, PROBABILMENTE QUELLO DELLE SHARED PREFERENCES E' SBAGLIATO.
                    //RISETTARLO A QUELLO PIU' RECENTE.
                    if(audioFromPref.getAudio() != audioFromServer.getAudio()) {
                        audioFromPref.setId(audioFromServer.getAudio());
                    }

                    if(!nonEmptyNonNull(audioFromServer.getVideo().getId())) {
                        Log.v("NoVideoREST","L'audio " +audioFromServer.getTitle() + "non ha video.");
                    } else if(!nonEmptyNonNull(audioFromServer.getVideo().getTitle())) {
                        Log.v("WrongVideoREST","Il video " +audioFromServer.getVideo().getId() + "non ha informazioni. \n" + audioFromServer.toString());

                    }
                    if(audioFromServer.getVideo().hashCode() != audioFromPref.getVideo().hashCode()) {
                        audioFromPref.setVideo(audioFromServer.getVideo());
                    }

                    found = true;
                }
            }

            if(!found) {
                listFromPref.add(audioFromServer);
            }
        }

        attachSameVideoToAudios(listFromPref);

        getDataManager().saveAudioList(listFromPref);

        return listFromPref;
    }

    private void attachSameVideoToAudios(List<LudoAudio> audioList) {
        for(LudoAudio audio : audioList) {
            LudoVideo ludoVideo = audio.getVideo();
            //SKIP IF NULL
            if(ludoVideo == null) {
                continue;
            }

            for(LudoAudio audioCompare : audioList) {
                LudoVideo ludoVideoCompare = audioCompare.getVideo();

                    //SKIP IF NULL

                    if (ludoVideoCompare == null || ludoVideo.equals(ludoVideoCompare)) {
                        audioCompare.setVideo(ludoVideo);
                    }
            }
        }
    }
}
