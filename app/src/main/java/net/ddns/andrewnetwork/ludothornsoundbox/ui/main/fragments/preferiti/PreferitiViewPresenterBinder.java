package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti;

import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.MvpPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.MvpView;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.video.PreferitiVideoViewPresenterBinder;

import java.util.List;

public interface PreferitiViewPresenterBinder {

    interface IPreferitiView<T> extends MvpView {

        void onPreferitoNonEsistente(T item);

        void onPreferitoRimossoSuccess();

        void onPreferitoRimossoFailed(String message);

        void onPreferitiListLoaded(List<T> list);

        void onPreferitiListError(List<T> list);

        void onPreferitiListEmpty();
    }

    interface IPreferitiPresenter<T, V extends IPreferitiView<T>> extends MvpPresenter<V> {

        List<T> getPreferitiListFromPref();

        void saveInPref(T item);

        void rimuoviPreferito(T item, PreferitiListAdapter.PreferitoDeletedListener<T> preferitoDeletedListener);
    }

}
