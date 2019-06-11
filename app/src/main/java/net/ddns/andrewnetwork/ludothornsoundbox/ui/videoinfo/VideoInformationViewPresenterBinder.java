package net.ddns.andrewnetwork.ludothornsoundbox.ui.videoinfo;

import android.content.SharedPreferences;

import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.Thumbnail;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.MvpPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.MvpView;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.PreferitiListAdapter;

import java.util.List;

public interface VideoInformationViewPresenterBinder {

    interface IVideoInformationView extends MvpView {

        void onThumbnailLoadSuccess(Thumbnail thumbnail);

        void onThumbnailLoadFailed();

        void onVideoInformationLoadFailed();

        void onVideoInformationLoadSuccess(LudoAudio audio);

        void onVideoByUrlLoadSuccess(LudoVideo video);

        void onPreferitoSavedSuccess(LudoVideo video);

        void onMaxVideoReached();

        void onPreferitoEsistente(LudoVideo video);

        void onPreferitoRimossoSuccess(LudoVideo item);

        void onPreferitoRimossoFailed();
    }

    interface IVideoInformationPresenter<V extends IVideoInformationView> extends MvpPresenter<V> {

        void getThumbnail(LudoVideo video);

        void getVideoInformation(LudoAudio id);

        void getVideoInformation(LudoVideo video);

        void getVideoByUrl(String url);

        List<LudoVideo> getPreferitiList();

        void aggiungiPreferito(LudoVideo video, PreferitiListAdapter.PreferitoDeletedListener<LudoVideo> preferitoDeletedListener);

        void rimuoviPreferito(LudoVideo item, PreferitiListAdapter.PreferitoDeletedListener<LudoVideo> preferitoDeletedListener);

        void registerOnSharedPreferencesChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener);

        void unregisterOnSharedPreferencesChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener);


        boolean isPreferito(LudoVideo video);
    }
}
