package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.audio;

import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.MvpPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.MvpView;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.PreferitiListAdapter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.PreferitiViewPresenterBinder;

import java.util.List;

public interface PreferitiAudioViewPresenterBinder {

    interface IPreferitiView extends PreferitiViewPresenterBinder.IPreferitiView<LudoAudio> {
    }

    interface IPreferitiPresenter<V extends IPreferitiView> extends PreferitiViewPresenterBinder.IPreferitiPresenter<LudoAudio, V> {

        void loadThumbnail(LudoVideo video, PreferitiListAdapter.ThumbnailLoadedListener thumbnailLoadedListener);

        void loadVideo(LudoAudio audio, PreferitiAudioListAdapter.VideoLoadedListener videoLoadedListener);
    }
}
