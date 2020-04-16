package net.ddns.andrewnetwork.ludothornsoundbox.data;

import android.content.Context;
import android.content.SharedPreferences;

import net.ddns.andrewnetwork.ludothornsoundbox.data.model.Channel;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.Thumbnail;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.VideoInformation;
import net.ddns.andrewnetwork.ludothornsoundbox.data.network.ApiHeader;
import net.ddns.andrewnetwork.ludothornsoundbox.data.network.ApiHelper;
import net.ddns.andrewnetwork.ludothornsoundbox.data.persistence.DatabaseHelper;
import net.ddns.andrewnetwork.ludothornsoundbox.data.prefs.PreferencesHelper;
import net.ddns.andrewnetwork.ludothornsoundbox.di.ApplicationContext;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.rx.RxBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;

import static net.ddns.andrewnetwork.ludothornsoundbox.utils.StringUtils.nonEmptyNonNull;


@Singleton
public class AppDataManager implements DataManager {

    private static final String TAG = "AppDataManager";

    private final Context mContext;
    private final PreferencesHelper mPreferencesHelper;
    private final ApiHelper mApiHelper;
    private final DatabaseHelper mDBHelper;
    private RxBus appBus;

    @Inject
    public AppDataManager(@ApplicationContext Context context, PreferencesHelper preferencesHelper, ApiHelper apiHelper, DatabaseHelper databaseHelper) {
        mContext = context;
        mPreferencesHelper = preferencesHelper;
        mApiHelper = apiHelper;
        mDBHelper = databaseHelper;
    }

    @Override
    public ApiHeader getApiHeader() {
        return mApiHelper.getApiHeader();
    }

    @Override
    public Observable<List<LudoVideo>> getVideoList(Channel channel) {
        return mApiHelper.getVideoList(channel);
    }

    @Override
    public Observable<Channel> getChannel(Channel channel) {
        return mApiHelper.getChannel(channel);
    }

    @Override
    public Single<Channel> getChannel(LudoVideo video) {
        return mApiHelper.getChannel(video);
    }

    @Override
    public Observable<Thumbnail> getThumbnail(LudoVideo video) {
        return mApiHelper.getThumbnail(video);
    }

    @Override
    public Observable<VideoInformation> getVideoInformation(LudoVideo video) {
        return mApiHelper.getVideoInformation(video);
    }

    @Override
    public Observable<List<LudoVideo>> getMoreVideos(Channel channel, Date beforeDate) {
        return mApiHelper.getMoreVideos(channel, beforeDate);
    }


    @Override
    public Observable<LudoAudio> getVideoById(LudoAudio audio) {
        if(!nonEmptyNonNull(audio.getVideo().getId())) {
            return Observable.create(emitter -> {
                emitter.onNext(audio);
                emitter.onComplete();
            });
        }

        return mApiHelper.getVideoById(audio);
    }

    @Override
    public Single<LudoVideo> getVideoById(String url) {
        return mApiHelper.getVideoById(url);
    }

    @Override
    public void saveAudioList(List<LudoAudio> audioList) {
        mPreferencesHelper.saveAudioList(audioList);

    }

    @Override
    public void removeAllVideosInPref() {
        mPreferencesHelper.removeAllVideosInPref();
    }

    @Override
    public void saveAudio(LudoAudio audio) {
        mPreferencesHelper.saveAudio(audio);
    }

    @Override
    public int getUsageCounter() {
        return mPreferencesHelper.getUsageCounter();
    }

    @Override
    public void incrementUsageCounter(int counter) {
        mPreferencesHelper.incrementUsageCounter(counter);
    }

    @Override
    public long getUsageThreshold() {
        return mPreferencesHelper.getUsageThreshold();
    }

    @Override
    public void setUsageThreshold(long threshold) {
        mPreferencesHelper.setUsageThreshold(threshold);
    }

    @Override
    public Long getCurrentUserId() {
        return mPreferencesHelper.getCurrentUserId();
    }

    @Override
    public String getAccessToken() {
        return mPreferencesHelper.getAccessToken();
    }

    @Override
    public List<LudoAudio> getPreferitiList() {
        return mPreferencesHelper.getPreferitiList() != null ? mPreferencesHelper.getPreferitiList() : new ArrayList<>();
    }

    @Override
    public List<LudoAudio> getAudioSavedList() {
        return mPreferencesHelper.getAudioSavedList() != null ? mPreferencesHelper.getAudioSavedList() : new ArrayList<>();
    }

    @Override
    public LudoAudio getVideoByIdInPref(LudoAudio audio) {
        return mPreferencesHelper.getVideoByIdInPref(audio);
    }

    @Override
    public boolean salvaPreferito(LudoAudio audio) {
        return mPreferencesHelper.salvaPreferito(audio);
    }

    @Override
    public boolean rimuoviPreferito(LudoAudio audio) {
        return mPreferencesHelper.rimuoviPreferito(audio);
    }

    @Override
    public void clearSharedPreferences() {
        mPreferencesHelper.clearSharedPreferences();
    }

    @Override
    public List<LudoVideo> getVideoPreferitiList() {
        return mPreferencesHelper.getVideoPreferitiList() != null ? mPreferencesHelper.getVideoPreferitiList() : new ArrayList<>();
    }

    @Override
    public void salvaVideoPreferito(LudoVideo video) {
        mPreferencesHelper.salvaVideoPreferito(video);
    }

    @Override
    public void saveVideoList(List<LudoVideo> videoList) {
        mPreferencesHelper.saveVideoList(videoList);
    }

    @Override
    public boolean rimuoviVideoPreferito(LudoVideo video) {
        return mPreferencesHelper.rimuoviVideoPreferito(video);
    }

    @Override
    public void registerOnSharedPreferencesChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        mPreferencesHelper.registerOnSharedPreferencesChangeListener(onSharedPreferenceChangeListener);
    }

    @Override
    public void unregisterOnSharedPreferencesChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        mPreferencesHelper.unregisterOnSharedPreferencesChangeListener(onSharedPreferenceChangeListener);
    }

    @Override
    public void saveAudioNascosto(LudoAudio audio) {
        mPreferencesHelper.saveAudioNascosto(audio);
    }

    @Override
    public void saveAudioListNascosti(List<LudoAudio> audioList) {
        mPreferencesHelper.saveAudioListNascosti(audioList);
    }

    @Override
    public List<LudoAudio> getAudioNascosti() {
        return mPreferencesHelper.getAudioNascosti() != null ? mPreferencesHelper.getAudioNascosti() : new ArrayList<>();
    }

    @Override
    public Single<List<LudoVideo>> searchVideosAllChannels(String searchString, List<Channel> channelList) {
        return mApiHelper.searchVideosAllChannels(searchString, channelList);
    }

    @Override
    public Single<List<LudoVideo>> searchMoreVideosAllChannels(String searchString, Date date, List<Channel> channelList) {
        return mApiHelper.searchMoreVideosAllChannels(searchString, date, channelList);
    }
}