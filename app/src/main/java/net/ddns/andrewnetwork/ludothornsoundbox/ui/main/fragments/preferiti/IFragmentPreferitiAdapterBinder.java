package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti;

import android.media.MediaPlayer;

import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.audio.PreferitiAudioListAdapter;

public interface IFragmentPreferitiAdapterBinder<T> {

    void loadThumbnail(T item, PreferitiListAdapter.ThumbnailLoadedListener thumbnailLoadedListener);

    void saveInPref(T item);

    void onPreferitoIntentDelete(T audio, PreferitiAudioListAdapter.PreferitoDeletedListener<T> preferitoDeletedListener);

    void cancelPreferitoIntentDelete();
}
