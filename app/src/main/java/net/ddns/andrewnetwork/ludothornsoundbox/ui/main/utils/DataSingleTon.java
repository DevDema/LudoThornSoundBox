package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.utils;

import android.media.MediaPlayer;

import javax.inject.Singleton;

public class DataSingleTon {

    private static DataSingleTon instance;
    private MediaPlayer mediaPlayer = new MediaPlayer();

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

}
