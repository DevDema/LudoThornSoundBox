package net.ddns.andrewnetwork.ludothornsoundbox.data.model;

import android.media.MediaPlayer;

import net.ddns.andrewnetwork.ludothornsoundbox.utils.view.MediaPlayerObserver;

import java.util.ArrayList;
import java.util.List;

public class DataSingleTon {

    public static final int ACTION_PLAYING = 0;
    public static final int ACTION_PAUSED = 1;
    public static final int ACTION_STOPPED = 2;
    public static final int ACTION_RESUMED = 3;
    public static final int ACTION_FINISHED = 4;
    private static DataSingleTon instance;
    private MediaPlayer mediaPlayer;
    private List<MediaPlayerObserver> observerList;

    public static DataSingleTon getInstance() {
        if(instance == null)
            instance = new DataSingleTon();
        return instance;
    }

    private DataSingleTon() {
        mediaPlayer = new MediaPlayer();
        observerList = new ArrayList<>();
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

    public void notifyAll(int status, LudoAudio audio) {
        for(MediaPlayerObserver observer : observerList) {
            switch (status) {
                case ACTION_RESUMED:
                    observer.notifyResumed();
                    break;
                case ACTION_STOPPED:
                    observer.notifyStopped();
                    break;
                case ACTION_PLAYING:
                    observer.notifyPlaying(audio);
                    break;
                case ACTION_PAUSED:
                    observer.notifyPaused();
                    break;
                case ACTION_FINISHED:
                    observer.notifyFinished();
                    break;

            }
        }
    }
}
