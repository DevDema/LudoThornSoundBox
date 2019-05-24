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

import android.content.SharedPreferences;

import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;

import java.util.List;

/**
 * Created by janisharali on 27/01/17.
 */

public interface PreferencesHelper {


    Long getCurrentUserId();

    String getAccessToken();

    List<LudoAudio> getPreferitiList();

    List<LudoAudio> getAudioSavedList();

    LudoAudio getVideoByIdInPref(LudoAudio audio);

    boolean salvaPreferito(LudoAudio audio);

    boolean rimuoviPreferito(LudoAudio audio);

    void saveAudioList(List<LudoAudio> audioList);

    void removeAllVideosInPref();

    void saveAudio(LudoAudio audio);

    int getUsageCounter();

    void incrementUsageCounter(int counter);

    long getUsageThreshold();

    void setUsageThreshold(long threshold);

    void clearSharedPreferences();

    List<LudoVideo> getVideoPreferitiList();

    void salvaVideoPreferito(LudoVideo video);

    void saveVideoList(List<LudoVideo> videoList);

    boolean rimuoviVideoPreferito(LudoVideo video);

    void registerOnSharedPreferencesChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener);

    void unregisterOnSharedPreferencesChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener);
}
