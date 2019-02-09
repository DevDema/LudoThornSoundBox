package net.ddns.andrewnetwork.ludothornsoundbox.ui.base.core.application;

import android.app.Activity;

public interface IBaseApplication {

    /**
     * Get last mActivity launched
     **/
    Activity getLastActivity();

    /**
     * Return true if app started for the first time
     **/
    boolean isFirstLaunch();

    /**
     * This method set the first time variable to false; after calling that
     *
     * @link:isFirstLaunch() returns false
     **/
    void onFirstLaunchCompleted();
}
