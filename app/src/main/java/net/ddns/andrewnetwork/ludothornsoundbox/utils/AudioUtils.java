package net.ddns.andrewnetwork.ludothornsoundbox.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;

import com.google.api.client.util.Charsets;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.BaseFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.utils.DataSingleTon;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public abstract class AudioUtils {

    public static List<LudoAudio> createAudioList(Context context) {
        List<LudoAudio> audioList = new ArrayList<>();
        final Resources resources = context.getResources();
        final Class<R.raw> c = R.raw.class;
        final Field[] fields = c.getDeclaredFields();
        final R.raw rawResources = new R.raw();

        for (Field field : fields) {
            String resourcename;
            int resourceid;
            try {
                resourcename = field.getName();
                if (resourcename.charAt(resourcename.length() - 1) != '_') {
                    int resourceInfoId = resources.getIdentifier(resourcename + "_", "raw", context.getPackageName());
                    LudoVideo ludoVideo = resourceInfoId != 0 ? JsonUtil.getGson().fromJson(readTextFile(resources.openRawResource(resourceInfoId)), LudoVideo.class): new LudoVideo();
                    resourceid = field.getInt(rawResources);
                    audioList.add(new LudoAudio(resourcename, resourceid, ludoVideo));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return audioList;
    }

    public static String readTextFile(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {

        }
        return outputStream.toString();
    }

    public static void playTrack(Context context, LudoAudio audio, MediaPlayer.OnCompletionListener onCompletionListener) {

        stopTrack();
        //TODO BUG DRAWABLE RESOURCE ID
        DataSingleTon.getInstance().setMediaPlayer(MediaPlayer.create(context, audio.getAudio()));

        DataSingleTon.getInstance().getMediaPlayer().setOnCompletionListener(onCompletionListener);
        DataSingleTon.getInstance().getMediaPlayer().start();
    }

    public static void stopTrack() {
        //TODO BUG CON IL MEDIAPLAYER.
        try {
            if (DataSingleTon.getInstance().getMediaPlayer() != null) {
                DataSingleTon.getInstance().getMediaPlayer().stop();
                DataSingleTon.getInstance().getMediaPlayer().reset();
                DataSingleTon.getInstance().getMediaPlayer().release();
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    public static LudoAudio findAudioById(List<LudoAudio> audioList, LudoAudio audio) {
        List<LudoAudio> audioList1 = new ArrayList<>(audioList);
        for (LudoAudio audioInList : audioList1) {
            if (audio.getAudio() == audioInList.getAudio()) {
                return audioInList;
            }
        }

        return audio;
    }

    public static void shareAudio(@NonNull BaseFragment fragment, @NonNull LudoAudio audio) {
        Uri fileUri = writeToExternalStorage(fragment, audio);

        if(fileUri == null) {
            fragment.showMessage(fragment.getResources().getText(R.string.generic_error_label).toString());
            return;
        }

        if(fragment.getContext() != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, fragment.getString(R.string.extra_audio_info));

            shareIntent.setType(fragment.getContext().getString(R.string.MIME_AUDIO_ANY));
            fragment.startActivity(Intent.createChooser(shareIntent, fragment.getResources().getText(R.string.send_audio_label)));
        }
    }

    private static Uri writeToExternalStorage(Fragment fragment, LudoAudio audio) {
        InputStream inputStream;
        FileOutputStream fileOutputStream;
        try {
            inputStream = fragment.getResources().openRawResource(audio.getAudio());
            File file = new File(Environment.getExternalStorageDirectory(), createAudioFileName(audio));
            fileOutputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, length);
            }



            inputStream.close();
            fileOutputStream.close();
            return Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String createAudioFileName(LudoAudio audio) {
        return audio.getTitle() + ".mp3";
    }
}

