package net.ddns.andrewnetwork.ludothornsoundbox.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.BaseFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.DataSingleTon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.gson.reflect.TypeToken;

import static net.ddns.andrewnetwork.ludothornsoundbox.data.model.DataSingleTon.ACTION_FINISHED;
import static net.ddns.andrewnetwork.ludothornsoundbox.data.model.DataSingleTon.ACTION_PLAYING;
import static net.ddns.andrewnetwork.ludothornsoundbox.utils.FileUtils.createAudioPath;

public abstract class AudioUtils {


    public static List<LudoAudio> createAudioList(Context context) {
        List<LudoAudio> audioList = new ArrayList<>();
        final AssetManager assetManager = context.getAssets();
        final String[] SUPPORTED_AUDIO_EXTENSIONS = {".mp3", ".wav"};
        final String[] SUPPORTED_TEXT_EXTENSIONS = {"_.txt", ".txt"};
        try {
            String[] list = assetManager.list(FileUtils.AudioAssetpath);

            if (list != null && list.length > 0) {
                for (String string : list) {
                    String endsWith = StringUtils.endsWith(string, SUPPORTED_AUDIO_EXTENSIONS);
                    if (endsWith != null) {
                        LudoVideo ludoVideo;
                        try {
                            InputStream isTxt = null;
                            int i = 0;
                            while (true) {
                                if (i >= SUPPORTED_TEXT_EXTENSIONS.length) {
                                    break;
                                }
                                try {
                                    isTxt = assetManager.open(createAudioPath(string.replace(endsWith, SUPPORTED_TEXT_EXTENSIONS[i])));
                                    break;
                                } catch (IOException e) {
                                    i++;
                                }
                            }

                            if(isTxt != null) {
                                String json = FileUtils.inputStreamToString(isTxt);
                                ludoVideo = JsonUtil.getGson().fromJson(json, new TypeToken<LudoVideo>() {}.getType());
                            } else {
                                Log.e("TxtFileException", "Txt for " + string + " not found.");
                                ludoVideo = new LudoVideo();
                            }

                        } catch (IOException e) {
                            Log.e("FolderException", "Folder not found.");
                            ludoVideo = new LudoVideo();
                            e.printStackTrace();
                        }

                        audioList.add(new LudoAudio(string, ludoVideo));
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return audioList;
    }


    public static void playTrack(Context context, LudoAudio audio) {

        stopTrack();
        MediaPlayer mediaPlayer = new MediaPlayer();
        final AssetManager assetManager = context.getAssets();

        try {
            AssetFileDescriptor afd = assetManager.openFd(createAudioPath(audio.getAudioFile()));
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        DataSingleTon.getInstance().setMediaPlayer(mediaPlayer);

        if (DataSingleTon.getInstance().getMediaPlayer() != null) {
            DataSingleTon.getInstance().getMediaPlayer().setOnCompletionListener(mp -> DataSingleTon.getInstance().notifyAll(ACTION_FINISHED, audio));
            DataSingleTon.getInstance().getMediaPlayer().prepareAsync();
            DataSingleTon.getInstance().getMediaPlayer().setOnPreparedListener(mp -> {
                mp.start();
                DataSingleTon.getInstance().notifyAll(ACTION_PLAYING, audio);
            });
        }

    }

    public static void stopTrack() {
        stopTrack(null);
    }

    public static void stopTrack(MediaPlayer.OnCompletionListener onCompletionListener) {
        try {
            if (DataSingleTon.getInstance().getMediaPlayer() != null) {
                DataSingleTon.getInstance().getMediaPlayer().stop();
                DataSingleTon.getInstance().getMediaPlayer().reset();
                DataSingleTon.getInstance().getMediaPlayer().release();

                DataSingleTon.getInstance().notifyAll(DataSingleTon.ACTION_STOPPED, null);
            }

            if (onCompletionListener != null) {
                onCompletionListener.onCompletion(DataSingleTon.getInstance().getMediaPlayer());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void attachSameVideoToAudios(List<LudoAudio> audioList) {
        for (LudoAudio audio : audioList) {
            LudoVideo ludoVideo = audio.getVideo();
            //SKIP IF NULL
            if (ludoVideo == null) {
                continue;
            }

            for (LudoAudio audioCompare : audioList) {
                LudoVideo ludoVideoCompare = audioCompare.getVideo();

                if (ludoVideoCompare == null || ludoVideo.equals(ludoVideoCompare)) {
                    audioCompare.setVideo(ludoVideo);
                }
            }
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

    public static LudoAudio findAudioByTitle(List<LudoAudio> audioList, LudoAudio audio) {
        List<LudoAudio> audioList1 = new ArrayList<>(audioList);
        for (LudoAudio audioInList : audioList1) {
            if (audio.getTitle().equals(audioInList.getTitle())) {
                return audioInList;
            }
        }

        return audio;
    }

    public static void shareAudio(@NonNull Activity context, @NonNull LudoAudio audio) {
        Uri fileUri;
        if (Build.VERSION.SDK_INT >= 24) {
            if (context != null) {
                writeToExternalStorage(context, audio);
                fileUri = FileProvider.getUriForFile(
                        context,
                        context.getApplicationContext().getPackageName() + ".utils.GenericFileProvider",
                        createAudioFile(audio)
                );
            } else {
                throw new IllegalArgumentException("fragment not attached to a context");
            }
        } else {
            fileUri = writeToExternalStorage(context, audio);
        }


        if (fileUri == null) {
            CommonUtils.showDialog(context, R.string.generic_error_label);
            return;
        }

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);

        shareIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.extra_audio_info));

        shareIntent.setType(context.getString(R.string.MIME_AUDIO_ANY));
        context.startActivity(Intent.createChooser(shareIntent, context.getResources().getString(R.string.send_audio_label)));

    }

    private static Uri writeToExternalStorage(Context context, LudoAudio audio) {
        InputStream inputStream;
        FileOutputStream fileOutputStream;
        final AssetManager assetManager = context.getAssets();
        try {
            inputStream = assetManager.open(createAudioPath(audio.getAudioFile()));
            File file = createAudioFile(audio);
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

    private static File createAudioFile(LudoAudio audio) {
        return new File(Environment.getExternalStorageDirectory(), createAudioFileName(audio));
    }

    private static String createAudioFileName(LudoAudio audio) {
        return audio.getTitle() + ".mp3";
    }

    public static boolean setAsRingtone(Context context, LudoAudio audio, int type) {
        byte[] buffer;
        final AssetManager assetManager = context.getAssets();
        int size = 0;

        try {
            InputStream fIn = assetManager.open(createAudioPath(audio.getAudioFile()));
            size = fIn.available();
            buffer = new byte[size];
            fIn.read(buffer);
            fIn.close();
        } catch (IOException e) {
            return false;
        }


        String filename = audio.getTitle();
        String exStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String path = (exStoragePath + "/media/alarms/");

        boolean exists = (new File(path)).exists();
        if (!exists) {
            new File(path).mkdirs();
        }

        FileOutputStream save;
        try {
            save = new FileOutputStream(path + filename);
            save.write(buffer);
            save.flush();
            save.close();
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }


        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path + filename + ".mp3"
                + Environment.getExternalStorageDirectory())));


        File k = new File(path, filename);

        ContentValues values = new ContentValues(4);
        long current = System.currentTimeMillis();
        values.put(MediaStore.MediaColumns.DATA, path + filename);
        values.put(MediaStore.MediaColumns.TITLE, filename);
        values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/mp3");

        //new
        values.put(MediaStore.Audio.Media.ARTIST, context.getString(R.string.ludo_thorn_label));
        values.put(MediaStore.Audio.Media.IS_RINGTONE, false);
        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
        values.put(MediaStore.Audio.Media.IS_ALARM, true);
        values.put(MediaStore.Audio.Media.IS_MUSIC, false);

        Uri uri = MediaStore.Audio.Media.getContentUriForPath(k
                .getAbsolutePath());

        context.getContentResolver().delete(
                uri,
                MediaStore.MediaColumns.DATA + "=\""
                        + k.getAbsolutePath() + "\"", null);
        // Insert it into the database
        Uri newUri = context.getContentResolver()
                .insert(uri, values);

        if (newUri == null) {
            return false;
        }

        try {
            RingtoneManager.setActualDefaultRingtoneUri(context, type, newUri);

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return false;
        }
        return true;
    }

    public static void resumeTrack() {
        DataSingleTon.getInstance().getMediaPlayer().start();
        DataSingleTon.getInstance().notifyAll(DataSingleTon.ACTION_RESUMED, null);
    }

    public static void pauseTrack() {
        DataSingleTon.getInstance().getMediaPlayer().pause();
        DataSingleTon.getInstance().notifyAll(DataSingleTon.ACTION_PAUSED, null);
    }

    public static String[] getAudioNameArray(Context context) {
        List<LudoAudio> audioList = createAudioList(context);

        return getAudioNameArray(audioList);
    }

    public static String[] getAudioNameArray(List<LudoAudio> audioList) {
        String[] strings = new String[audioList.size()];

        for (int i = 0; i < audioList.size(); i++) {
            LudoAudio audio = audioList.get(i);
            strings[i] = audio.getTitle();
        }

        return strings;
    }
}

