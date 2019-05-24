package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.audio;

import android.media.MediaPlayer;

import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.IFragmentPreferitiAdapterBinder;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.PreferitiListAdapter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.audio.PreferitiAudioListAdapter.VideoLoadedListener;

public interface IFragmentAudioPreferitiAdapterBinder extends IFragmentPreferitiAdapterBinder<LudoAudio> {

    void playAudio(LudoAudio audio);

    void stopAudio(LudoAudio audio);

    void setOnCompletionListener(MediaPlayer.OnCompletionListener onCompletionListener);

    void loadVideo(LudoAudio audio, VideoLoadedListener videoLoadedListener);

    void apriVideo(LudoVideo item);
}
