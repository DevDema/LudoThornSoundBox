package net.ddns.andrewnetwork.ludothornsoundbox.data;

import android.content.Context;

import net.ddns.andrewnetwork.ludothornsoundbox.data.network.ApiHeader;
import net.ddns.andrewnetwork.ludothornsoundbox.data.network.ApiHelper;
import net.ddns.andrewnetwork.ludothornsoundbox.data.persistence.DatabaseHelper;
import net.ddns.andrewnetwork.ludothornsoundbox.data.prefs.PreferencesHelper;
import net.ddns.andrewnetwork.ludothornsoundbox.di.ApplicationContext;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.rx.RxBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Single;


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
    public Long getCurrentUserId() {
        return mPreferencesHelper.getCurrentUserId();
    }

    @Override
    public String getAccessToken() {
        return mPreferencesHelper.getAccessToken();
    }
}