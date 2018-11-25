package net.ddns.andrewnetwork.ludothornsoundbox.controller;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.Log;

import net.ddns.andrewnetwork.ludothornsoundbox.model.FavoriteAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public abstract class FavoriteController {

    public static boolean saveFavorite(Context context, FavoriteAudio audio) {
        JSONObject audiojson = new JSONObject();
        try {
            audiojson.put("title", audio.getTitle());
            audiojson.put("audio", audio.getAudio());
            audiojson.put("order", audio.getOrder());
            JSONArray beforeaudio = loadJsonArray(context);
            beforeaudio.put(audiojson);
            SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            prefsEditor.putString("FavoriteList", beforeaudio.toString());
            prefsEditor.commit();
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteFavorite(Context context, FavoriteAudio audio) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        JSONObject audiojson;
        try {
            JSONArray jsonArray = loadJsonArray(context);
            JSONArray newJsonArray = new JSONArray();
            for (int i = 0; i < jsonArray.length(); i++) {
                audiojson = jsonArray.getJSONObject(i);
                FavoriteAudio favaudio;
                if(audiojson.has("order")) favaudio = new FavoriteAudio(audiojson.getString("title"), Integer.parseInt(audiojson.get("audio").toString()),Integer.parseInt(audiojson.getString("order")));
                else favaudio = new FavoriteAudio(audiojson.getString("title"),Integer.parseInt(audiojson.get("audio").toString()));//,Integer.parseInt(audiojson.getString("order"))));
                if(favaudio.getAudio() != audio.getAudio()) newJsonArray.put(audiojson);
            }
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            prefsEditor.putString("FavoriteList", newJsonArray.toString());
            prefsEditor.commit();
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static JSONArray loadJsonArray(Context context) throws JSONException {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        String list = appSharedPrefs.getString("FavoriteList","");
        if(list.isEmpty()) return new JSONArray();
        return new JSONArray(list);
    }


    public static List<FavoriteAudio> loadFavorite(Context context) throws JSONException {
        JSONArray jsonArray = loadJsonArray(context);
        JSONObject object;
        ArrayList<FavoriteAudio> listdata = new ArrayList<>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                object = jsonArray.getJSONObject(i);
                if(object.has("order")) listdata.add(new FavoriteAudio(object.getString("title"), Integer.parseInt(object.getString("audio")),Integer.parseInt(object.getString("order"))));
                else listdata.add(new FavoriteAudio(object.getString("title"),Integer.parseInt(object.getString("audio"))));
            }
        }
        return listdata;
    }

    public static boolean alreadyExists(Context context, FavoriteAudio audio) throws JSONException {
        int audionumber = audio.getAudio();
        List<FavoriteAudio> list = loadFavorite(context);
        if(list == null || list.isEmpty()) {
            return false;
        }
        for (FavoriteAudio audio1 : list )
        {
            if (audio1.getAudio() == audionumber) return true;
        }
        return false;
    }

    public static boolean isResourceIdInPackage(Context context, int resId){
         String packageName = "net.ddns.andrewnetwork.ludothornsoundbox";
        if(packageName == null || resId == 0){
            return false;
        }

        Resources res = null;
        if(packageName.equals(context.getPackageName())){
            res = context.getResources();
        }else{
            try{
                res = context.getPackageManager().getResourcesForApplication(packageName);
            }catch(PackageManager.NameNotFoundException e){
                Log.v("Ludo", packageName + "does not contain " + resId + " ... " + e.getMessage());
            }
        }

        if(res == null)
            return false;

        return isResourceIdInResources(context, resId);
    }

    private static boolean isResourceIdInResources(Context context, int resId){

        try{
            context.getResources().getResourceName(resId);
            return true;

        }catch (Resources.NotFoundException e){
            return false;
        }
    }

}
