package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home;

import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.MvpPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.MvpView;

import java.util.List;

public interface HomeViewPresenterBinder {

    interface IHomeView extends MvpView {

        void onAudioListReceived(List<LudoAudio> audioList);

        void onPreferitoEsistente(LudoAudio audio);

        void onPreferitoSalvataggioSuccess();

        void onPreferitoSalvataggioFailed(String messaggio);

        void onMaxAudioReached();

        void onVideoInformationNotLoaded();
    }

    interface IHomePresenter<V extends IHomeView> extends MvpPresenter<V> {

        void saveAudioListToPref(List<LudoAudio> audioList);

        void salvaPreferito(LudoAudio audio);

        void getVideoInformationForAudios(List<LudoAudio> audioList);

        void salvaAudio(LudoAudio audio);

        List<LudoAudio> getAudioListFromPreferences();

        void saveAudioListInPref(List<LudoAudio> audioList);

        void saveAudioNascosto(LudoAudio audio);

        List<LudoAudio> getHiddenAudioList();
    }
}
