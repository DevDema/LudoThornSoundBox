package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.random;

import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.MvpPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.MvpView;

import java.util.List;

public interface RandomViewPresenterBinder {

    interface IRandomView extends MvpView {

    }

    interface IRandomPresenter<V extends IRandomView> extends MvpPresenter<V> {
        List<LudoAudio> getAudioListFromPref();
    }
}
