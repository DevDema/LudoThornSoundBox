package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home;

import net.ddns.andrewnetwork.ludothornsoundbox.data.DataManager;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.BasePresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.HomeViewPresenterBinder.IHomePresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.HomeViewPresenterBinder.IHomeView;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class HomePresenter<V extends IHomeView> extends BasePresenter<V> implements IHomePresenter<V> {

    @Inject
    public HomePresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }


    @Override
    public void salvaPreferito(LudoAudio audio) {
        List<LudoAudio> preferitiList = getDataManager().getPreferitiList() != null ? getDataManager().getPreferitiList() : new ArrayList<>();

        //CONTROLLA SE ESISTE GIA'
        for(LudoAudio audioInList : preferitiList) {
            if(audioInList.getAudio() == audio.getAudio()) {
                getMvpView().onPreferitoEsistente(audio);
                return;
            }
        }

        //SE NON ESISTE, AGGIUNGI.
        try {
            getDataManager().salvaPreferito(audio);
            getMvpView().onPreferitoSalvataggioSuccess();
        } catch (Exception e) {
           e.printStackTrace();
            getMvpView().onPreferitoSalvataggioFailed(e.getCause().getMessage());
        }
    }
}
