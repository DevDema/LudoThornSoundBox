package net.ddns.andrewnetwork.ludothornsoundbox.controller;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;

import net.ddns.andrewnetwork.ludothornsoundbox.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.model.LudoVideoResponse;
import net.ddns.andrewnetwork.ludothornsoundbox.model.Thumbnail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MemoryManager {
    JSONArray thumbnailListJson;
    SharedPreferences appSharedPrefs;
    ContextWrapper cw;
    SharedPreferences.Editor prefsEditor;
    String imageDir = "thumbnails";

    public MemoryManager(Context context) throws JSONException {
        appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        prefsEditor = appSharedPrefs.edit();
        cw = new ContextWrapper(context.getApplicationContext());
        thumbnailListJson = loadFromMemory();
    }
    public boolean clearMemory() {
        File dir = new File(imageDir);
        if (dir.isDirectory()) {
            dir.delete();
            return removeAllVideosFromMemory();
        }
        return false;
    }

    public boolean removeAllVideosFromMemory() {
        return prefsEditor.remove("ThumbnailList").commit();
    }
    public boolean saveToMemory( LudoVideo video) throws JSONException {
        String location = saveToInternalStorage(video);
        if(location!=null) {
            Thumbnail thumbnail = video.getThumbnail();
            JSONObject thumbnailjson = new JSONObject();
            thumbnailjson.put("videoid",video.getId());
            thumbnailjson.put("url",thumbnail.getUrl());
            thumbnailjson.put("image",location);
            thumbnailListJson.put(thumbnailjson);

            prefsEditor.putString("ThumbnailList", thumbnailListJson.toString());
            if(prefsEditor.commit()) return true;
        }
        return false;
    }

    public boolean videoJSONExists(String id) throws JSONException {
        JSONArray videoArray = loadFromMemory();
        for (int i=0; i < videoArray.length(); i++) {
            JSONObject videoJson = videoArray.getJSONObject(i);
            if(videoJson.get("videoid").equals(id)) return true;
        }
        return false;
    }

    public boolean saveToMemory(List<LudoVideo> videoList) throws JSONException {
        for(LudoVideo video : videoList) {
            if(!videoJSONExists(video.getId())) {
                String location = saveToInternalStorage(video);
                if (location != null) {
                    Thumbnail thumbnail = video.getThumbnail();
                    JSONObject thumbnailjson = new JSONObject();
                    thumbnailjson.put("videoid", video.getId());
                    thumbnailjson.put("url", thumbnail.getUrl());
                    thumbnailjson.put("image", location);
                    thumbnailListJson.put(thumbnailjson);
                }
            }
        }
        prefsEditor.putString("ThumbnailList", thumbnailListJson.toString());
        if (prefsEditor.commit()) return true;
        return false;
    }

    public JSONArray loadFromMemory() throws JSONException {
        String list = appSharedPrefs.getString("ThumbnailList","");
        if(list.isEmpty()) return new JSONArray();
        return new JSONArray(list);
    }

    public List<LudoVideoResponse> getVideos() throws JSONException {
        JSONArray videoArray = loadFromMemory();
        List<LudoVideoResponse> thumbnailsPathsList = new ArrayList<>();
        for (int i=0; i < videoArray.length(); i++) {
            JSONObject videoJson = videoArray.getJSONObject(i);
            thumbnailsPathsList.add(new LudoVideoResponse((String) videoJson.get("videoid"), (String) videoJson.get("url"),(String) videoJson.get("image")));
        }
        return thumbnailsPathsList;
    }

    public String saveToInternalStorage(LudoVideo video) {
        Thumbnail thumbnail = null;
        Bitmap bitmapImage = null;
        if (video != null) thumbnail = video.getThumbnail();
        if (thumbnail != null) bitmapImage = video.getThumbnail().getImage();
            if(bitmapImage!=null) {
            // path to /data/data/yourapp/app_data/imageDir
            File directory = cw.getDir(imageDir, Context.MODE_PRIVATE);
            // Create imageDir
            File mypath = new File(directory, video.getId());

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(mypath);
                // Use the compress method on the BitMap object to write image to the OutputStream
                bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return mypath.getAbsolutePath();
        }
        return null;
    }
}
