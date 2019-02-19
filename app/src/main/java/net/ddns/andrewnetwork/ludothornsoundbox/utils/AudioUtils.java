package net.ddns.andrewnetwork.ludothornsoundbox.utils;

import android.content.Context;
import android.media.MediaPlayer;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class AudioUtils {

    public static List<LudoAudio> createAudioList() {
        List<LudoAudio> audioList = new ArrayList<>();
        final Class<R.raw> c = R.raw.class;
        final Field[] fields = c.getDeclaredFields();
        final R.raw rawResources = new R.raw();

        for (Field field : fields) {
            String resourcename;
            int resourceid;
            try {
                resourcename = field.getName();
                resourceid = field.getInt(rawResources);
                audioList.add(new LudoAudio(resourcename, resourceid));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return audioList;
    }

    public static void playTrack(Context context, MediaPlayer mediaPlayer, LudoAudio audio) {
       stopTrack(mediaPlayer);

       mediaPlayer = MediaPlayer.create(context, audio.getAudio());

       mediaPlayer.start();
    }

    public static void stopTrack(MediaPlayer mediaPlayer) {
        //TODO BUG CON IL MEDIAPLAYER.
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }
}
