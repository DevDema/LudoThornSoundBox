package net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.activity.hiddenaudio;

import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.MvpPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.MvpView;

import java.util.List;


public interface SettingsHiddenAudioViewPresenterBinder {

    interface ISettingsHiddenAudioView extends MvpView {

    }

    interface ISettingsHiddenAudioPresenter<V extends ISettingsHiddenAudioView> extends MvpPresenter<V> {

        List<LudoAudio> getAudioList();

        void salvaListaAudio(List<LudoAudio> audios);
    }
}
