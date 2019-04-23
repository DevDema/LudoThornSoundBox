package net.ddns.andrewnetwork.ludothornsoundbox.ui.base.core.application;

import android.app.Activity;
import android.content.Context;

import androidx.multidex.MultiDex;

public abstract class BaseApplication extends android.app.Application implements IBaseApplication {

    public static final String TAG = "BaseApplication";

    private Delegate mDelegate;

    @Override
    public void onCreate() {
        super.onCreate();
        mDelegate = new Delegate(this);
    }

    /**
     * Get last mActivity launched
     **/
    @Override
    public Activity getLastActivity() {
        return mDelegate.getLastActivity();
    }

    /**
     * Return true if app started for the first time
     **/
    @Override
    public boolean isFirstLaunch() {
        return mDelegate.isFirstLaunch();
    }

    /**
     * This method set the first time variable to false; after calling that
     *
     * @link:isFirstLaunch() returns false
     **/
    @Override
    public void onFirstLaunchCompleted() {
        mDelegate.onFirstLaunchCompleted();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
