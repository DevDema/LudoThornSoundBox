package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti;

import android.media.MediaPlayer;

import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.PreferitiListAdapter.ThumbnailLoadedListener;

public interface IFragmentAdapterBinder {

    void playAudio(LudoAudio audio);

    void stopAudio(LudoAudio audio);

    void loadThumbnail(LudoAudio audio, ThumbnailLoadedListener thumbnailLoadedListener);

    void setOnCompletionListener(MediaPlayer.OnCompletionListener onCompletionListener);
}
