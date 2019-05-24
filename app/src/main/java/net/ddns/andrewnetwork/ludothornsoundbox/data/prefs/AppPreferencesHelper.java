/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package net.ddns.andrewnetwork.ludothornsoundbox.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;

import com.google.gson.reflect.TypeToken;

import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.di.ApplicationContext;
import net.ddns.andrewnetwork.ludothornsoundbox.di.PreferenceInfo;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.AppConstants;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.AudioUtils;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.JsonUtil;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.VideoUtils;


import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.inject.Inject;
import javax.inject.Singleton;

import static net.ddns.andrewnetwork.ludothornsoundbox.utils.AppUtils.DAYS_BEFORE_ASKING_FEEDBACK;


@Singleton
public class AppPreferencesHelper implements PreferencesHelper {

    private static final String PREF_KEY_CURRENT_USER_ID = "PREF_KEY_CURRENT_USER_ID";
    private static final String PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN";
    public static final String PREF_KEY_PREFERITI_AUDIO = "PREF_KEY_PREFERITI_AUDIO";
    private static final String PREF_KEY_AUDIO = "PREF_KEY_AUDIO";
    private static final String KEY_USAGE_COUNTER = "KEY_USAGE_COUNTER";
    private static final String KEY_USAGE_THRESOLD = "KEY_USAGE_THRESOLD";
    public static final String PREF_KEY_PREFERITI_VIDEO = "PREF_KEY_PREFERITI_VIDEO";

    private final SharedPreferences mPrefs;

    @Inject
    public AppPreferencesHelper(@ApplicationContext Context context,
                                @PreferenceInfo String prefFileName) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public Long getCurrentUserId() {
        long userId = mPrefs.getLong(PREF_KEY_CURRENT_USER_ID, AppConstants.NULL_INDEX);
        return userId == AppConstants.NULL_INDEX ? null : userId;
    }

    @Override
    public String getAccessToken() {
        return mPrefs.getString(PREF_KEY_ACCESS_TOKEN, null);
    }

    @Override
    public List<LudoAudio> getPreferitiList() {
        String serializedList = mPrefs.getString(PREF_KEY_PREFERITI_AUDIO, "");
        return JsonUtil.getGson().fromJson(serializedList, new TypeToken<List<LudoAudio>>() {
        }.getType());
    }

    @Override
    public List<LudoAudio> getAudioSavedList() {
        String serializedList = mPrefs.getString(PREF_KEY_AUDIO, "");
        List<LudoAudio> audioList = JsonUtil.getGson().fromJson(serializedList, new TypeToken<List<LudoAudio>>() {}.getType());
        return audioList != null ? audioList : new ArrayList<>();
    }

    @Override
    public LudoAudio getVideoByIdInPref(LudoAudio audio) {
        List<LudoAudio> audioList = getAudioSavedList();

        return AudioUtils.findAudioById(audioList, audio);
    }

    @Override
    public boolean salvaPreferito(LudoAudio audio) {
        try {
            List<LudoAudio> audioList = getPreferitiList() != null ? getPreferitiList() : new ArrayList<>();

            int index = !audioList.isEmpty() ? audioList.size()-1 : 0;

            for(ListIterator<LudoAudio> iterator = audioList.listIterator(); iterator.hasNext();) {
                if(audio.getAudio() == iterator.next().getAudio()) {
                    iterator.remove();
                    index = iterator.nextIndex();
                }
            }

            if (audioList.size() >= 5) {
                throw new IllegalArgumentException();
            }

            audioList.add(index, audio);

            String audioListString = JsonUtil.getGson().toJson(audioList);

            mPrefs.edit().putString(PREF_KEY_PREFERITI_AUDIO, audioListString).apply();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean rimuoviPreferito(LudoAudio audio) {
        try {
            List<LudoAudio> audioList = getPreferitiList() != null ? getPreferitiList() : new ArrayList<>();

            boolean result = audioList.remove(AudioUtils.findAudioById(audioList, audio));

            String audioListString = JsonUtil.getGson().toJson(audioList);

            mPrefs.edit().putString(PREF_KEY_PREFERITI_AUDIO, audioListString).apply();

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void saveAudioList(List<LudoAudio> audioList) {

        String audioListString = JsonUtil.getGson().toJson(audioList);

        mPrefs.edit().putString(PREF_KEY_AUDIO, audioListString).apply();

    }

    @Override
    public void removeAllVideosInPref() {
        mPrefs.edit().remove(PREF_KEY_AUDIO).apply();
    }

    @Override
    public void saveAudio(LudoAudio audio) {
        List<LudoAudio> audioList = getAudioSavedList();

        if(audioList.contains(audio)) {
            if (audioList.remove(AudioUtils.findAudioById(audioList, audio))) {
                audioList.add(audio);
            }
        } else {
            audioList.add(audio);
        }

        saveAudioList(audioList);
    }

    @Override
    public int getUsageCounter() {
        return mPrefs.getInt(KEY_USAGE_COUNTER, 0);
    }

    @Override
    public void incrementUsageCounter(int counter) {
        mPrefs.edit().putInt(KEY_USAGE_COUNTER, counter).apply();

    }

    @Override
    public long getUsageThreshold() {
        return mPrefs.getLong(KEY_USAGE_THRESOLD, DAYS_BEFORE_ASKING_FEEDBACK);
    }

    @Override
    public void setUsageThreshold(long threshold) {
        mPrefs.edit().putLong(KEY_USAGE_THRESOLD, threshold).apply();
    }

    @Override
    public void clearSharedPreferences() {
        mPrefs.edit().clear().apply();
    }

    @Override
    public List<LudoVideo> getVideoPreferitiList() {
        String serializedList = mPrefs.getString(PREF_KEY_PREFERITI_VIDEO, "");
        return JsonUtil.getGson().fromJson(serializedList, new TypeToken<List<LudoVideo>>() {
        }.getType());
    }

    @Override
    public void salvaVideoPreferito(LudoVideo video) {
        List<LudoVideo> videoList = getVideoPreferitiList() != null ? getVideoPreferitiList() : new ArrayList<>();

        video.setPreferito(true);

        if(videoList.contains(video)) {
            if (videoList.remove(VideoUtils.findById(videoList, video))) {

                videoList.add(video);
            }
        } else {
            videoList.add(video);
        }

        saveVideoList(videoList);
    }

    @Override
    public void saveVideoList(List<LudoVideo> videoList) {

        String videoListString = JsonUtil.getGson().toJson(videoList);

        mPrefs.edit().putString(PREF_KEY_PREFERITI_VIDEO, videoListString).apply();
    }

    @Override
    public boolean rimuoviVideoPreferito(LudoVideo video) {
        try {
            List<LudoVideo> videoList = getVideoPreferitiList() != null ? getVideoPreferitiList() : new ArrayList<>();

            boolean result = videoList.remove(VideoUtils.findById(videoList, video));

            String videoListString = JsonUtil.getGson().toJson(videoList);

            mPrefs.edit().putString(PREF_KEY_PREFERITI_VIDEO, videoListString).apply();

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void registerOnSharedPreferencesChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        mPrefs.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    @Override
    public void unregisterOnSharedPreferencesChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        mPrefs.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }
}
