package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.utils;

import android.media.MediaPlayer;

import net.ddns.andrewnetwork.ludothornsoundbox.utils.view.AudioPlayer;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.view.MediaPlayerObserver;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

public class DataSingleTon {

    public static final int ACTION_PLAYING = 0;
    public static final int ACTION_PAUSED = 1;
    public static final int ACTION_STOPPED = 2;
    public static final int ACTION_RESUMED = 3;
    private static DataSingleTon instance;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private List<MediaPlayerObserver> observerList = new ArrayList<>();

    public static DataSingleTon getInstance() {
        if(instance == null)
            instance = new DataSingleTon();
        return instance;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public void registerObserver(MediaPlayerObserver mediaPlayerObserver) {
        observerList.add(mediaPlayerObserver);
    }

    public void notifyAll(int status) {
        for(MediaPlayerObserver observer : observerList) {
            switch (status) {
                case ACTION_RESUMED:
                    observer.notifyResumed();
                    break;
                case ACTION_STOPPED:
                    observer.notifyStopped();
                    break;
                case ACTION_PLAYING:
                    observer.notifyPlaying();
                    break;
                case ACTION_PAUSED:
                    observer.notifyPaused();
                    break;

            }
        }
    }
}
