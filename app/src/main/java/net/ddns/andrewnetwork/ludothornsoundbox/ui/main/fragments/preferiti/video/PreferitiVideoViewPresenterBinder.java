package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.video;

import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.MvpPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.MvpView;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.PreferitiListAdapter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.PreferitiViewPresenterBinder;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.audio.PreferitiAudioListAdapter;

import java.util.List;

public interface PreferitiVideoViewPresenterBinder {

    interface IPreferitiView extends PreferitiViewPresenterBinder.IPreferitiView<LudoVideo> {
    }

    interface IPreferitiPresenter<V extends IPreferitiView> extends PreferitiViewPresenterBinder.IPreferitiPresenter<LudoVideo, V> {

        void loadThumbnail(LudoVideo video, PreferitiListAdapter.ThumbnailLoadedListener thumbnailLoadedListener);

        void loadChannel(LudoVideo video, IFragmentVideoPreferitiAdapterBinder.OnChannelLoadedListener onChannelLoadedListener);
    }
}
