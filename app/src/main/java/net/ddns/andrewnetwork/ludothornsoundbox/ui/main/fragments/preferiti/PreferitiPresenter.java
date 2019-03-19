package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti;

import net.ddns.andrewnetwork.ludothornsoundbox.data.DataManager;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.BasePresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.HomeViewPresenterBinder.IHomePresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.HomeViewPresenterBinder.IHomeView;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.PreferitiViewPresenterBinder.IPreferitiView;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class PreferitiPresenter<V extends IPreferitiView> extends BasePresenter<V> implements PreferitiViewPresenterBinder.IPreferitiPresenter<V> {

    @Inject
    public PreferitiPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }


    @Override
    public List<LudoAudio> getPreferitiList() {
        return getDataManager().getPreferitiList();
    }

    @Override
    public void rimuoviPreferito(LudoAudio audio) {
        List<LudoAudio> preferitiList = getDataManager().getPreferitiList() != null ? getDataManager().getPreferitiList() : new ArrayList<>();
        //CONTROLLA SE ESISTE GIA'
        if(!audioExists(preferitiList, audio)) {
            getMvpView().onPreferitoNonEsistente(audio);
            return;
        }

        //SE ESISTE, RIMUOVI.
        try {
            if(getDataManager().rimuoviPreferito(audio)) {
                getMvpView().onPreferitoRimossoSuccess();
            }
        } catch (Exception e) {
            e.printStackTrace();
            getMvpView().onPreferitoRimossoFailed(e.getCause().getMessage());
        }
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
