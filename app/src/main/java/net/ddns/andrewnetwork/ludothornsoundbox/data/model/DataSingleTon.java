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
    private int currentStatus = ACTION_FINISHED;
    private LudoAudio currentAudio;

    public static DataSingleTon getInstance() {
        if (instance == null)
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

        initPlayer(mediaPlayerObserver);
    }

    public void notifyAll(int status, LudoAudio audio) {

        this.currentStatus = status;
        this.currentAudio = audio;

        for (MediaPlayerObserver observer : observerList) {
            notifyPlayer(observer, audio, status);
        }
    }

    private void notifyPlayer(MediaPlayerObserver observer, LudoAudio audio, int status) {
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

    private void initPlayer(MediaPlayerObserver mediaPlayerObserver) {
        notifyPlayer(mediaPlayerObserver, currentAudio, currentStatus);
    }
}
