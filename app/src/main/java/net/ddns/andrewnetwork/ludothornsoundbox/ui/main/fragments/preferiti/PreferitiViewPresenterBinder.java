package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti;

import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.MvpPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.MvpView;

import java.util.List;

public interface PreferitiViewPresenterBinder {

    interface IPreferitiView extends MvpView {
        void onPreferitoNonEsistente(LudoAudio audio);

        void onPreferitoRimossoSuccess();

        void onPreferitoRimossoFailed(String message);
    }

    interface IPreferitiPresenter<V extends IPreferitiView> extends MvpPresenter<V> {

        List<LudoAudio> getPreferitiList();

        void rimuoviPreferito(LudoAudio audio);
    }
}
