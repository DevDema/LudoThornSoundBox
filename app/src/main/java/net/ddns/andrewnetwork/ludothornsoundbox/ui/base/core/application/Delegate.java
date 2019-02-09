package net.ddns.andrewnetwork.ludothornsoundbox.ui.base.core.application;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;

import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.core.extras.ActivitiesManager;

import static android.content.Context.MODE_PRIVATE;

class Delegate implements IBaseApplication {

    public static final String TAG = "IBaseApplication.Delegate";

    private static final String ARG_IS_FIRST_LAUNCH = "ARG_IS_FIRST_LAUNCH";

    private ActivitiesManager mActivitiesManager;

    private Application mApplication;

    Delegate(Application application) {
        mApplication = application;
        ActivitiesManager.init(application);
        mActivitiesManager = ActivitiesManager.getInstance();
    }

    public Activity getLastActivity() {
        return mActivitiesManager.getLastInstance();
    }

    public boolean isFirstLaunch() {
        SharedPreferences preferences = mApplication.getSharedPreferences(TAG, MODE_PRIVATE);
        return preferences.getBoolean(ARG_IS_FIRST_LAUNCH, true);
    }

    public void onFirstLaunchCompleted() {
        SharedPreferences preferences = mApplication.getSharedPreferences(TAG, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(ARG_IS_FIRST_LAUNCH, false);
        editor.apply();
    }
}
