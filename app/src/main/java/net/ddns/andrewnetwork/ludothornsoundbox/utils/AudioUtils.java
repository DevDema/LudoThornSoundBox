package net.ddns.andrewnetwork.ludothornsoundbox.utils;

import android.content.Context;
import android.media.MediaPlayer;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.utils.DataSingleTon;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
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

    public static void playTrack(Context context, LudoAudio audio, MediaPlayer.OnCompletionListener onCompletionListener) {

        stopTrack();

        DataSingleTon.getInstance().setMediaPlayer(MediaPlayer.create(context, audio.getAudio()));

        DataSingleTon.getInstance().getMediaPlayer().setOnCompletionListener(onCompletionListener);
        DataSingleTon.getInstance().getMediaPlayer().start();
    }

    public static void stopTrack() {
        //TODO BUG CON IL MEDIAPLAYER.
        if (DataSingleTon.getInstance().getMediaPlayer() != null && DataSingleTon.getInstance().getMediaPlayer().isPlaying()) {
            DataSingleTon.getInstance().getMediaPlayer().stop();
            DataSingleTon.getInstance().getMediaPlayer().reset();
            DataSingleTon.getInstance().getMediaPlayer().release();
        }
    }

    public static void sortBy(List<LudoAudio> arrayList, int criterion) {
        switch (criterion) {
            case 1:
                Collections.sort(arrayList, LudoAudio.COMPARE_BY_NAME);
                break;
            case 2:
                Collections.sort(arrayList, LudoAudio.COMPARE_BY_DATE);
                break;
            default:
                break;
        }
    }

    public static void reverse(List<LudoAudio> arrayList) {
        Collections.reverse(arrayList);
    }
}

