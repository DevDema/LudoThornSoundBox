package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home;

import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.MvpPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.MvpView;

public interface HomeViewPresenterBinder {

    interface IHomeView extends MvpView {

        void onPreferitoEsistente(LudoAudio audio);

        void onPreferitoSalvataggioSuccess();

        void onPreferitoSalvataggioFailed(String messaggio);
    }

    interface IHomePresenter<V extends IHomeView> extends MvpPresenter<V> {

        void salvaPreferito(LudoAudio audio);
    }
}
