package net.ddns.andrewnetwork.ludothornsoundbox.ui.base;

import android.content.Intent;
import android.os.Bundle;

import net.ddns.andrewnetwork.ludothornsoundbox.MvpApp;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.core.extras.LifeCycleListener;

class ActivitiesStatusManager implements LifeCycleListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {
        MvpApp.activityResumed();
    }

    @Override
    public void onStop() {

    }

    @Override
    public void onPause() {
        MvpApp.activityPaused();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
