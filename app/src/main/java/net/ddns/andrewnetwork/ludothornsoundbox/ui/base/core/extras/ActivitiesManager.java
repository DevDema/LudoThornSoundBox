package net.ddns.andrewnetwork.ludothornsoundbox.ui.base.core.extras;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import java.util.Stack;

public class ActivitiesManager implements Application.ActivityLifecycleCallbacks {

    public static final String TAG = "ActivitiesManager";

    private static final int HEAD = 0;

    private static ActivitiesManager instance;

    private Stack<Activity> mActivityStack;

    public static void init(Application app) {
        if (instance == null) {
            instance = new ActivitiesManager();
            app.registerActivityLifecycleCallbacks(instance);
        }
    }

    public static ActivitiesManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("You need to call init() method first");
        }
        return instance;
    }

    private ActivitiesManager() {
        mActivityStack = new Stack<>();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        Log.d(TAG, "A new Activity created (added to stack): " + activity.getLocalClassName());
        mActivityStack.add(HEAD, activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.d(TAG, "Activity started: " + activity.getLocalClassName());

    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.d(TAG, "Activity resumed: " + activity.getLocalClassName());
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.d(TAG, "Activity paused: " + activity.getLocalClassName());
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.d(TAG, "Activity stopped: " + activity.getLocalClassName());
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        Log.d(TAG, "Save Instance State called: " + activity.getLocalClassName());
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.d(TAG, "Activity destroyed (remove): " + activity.getLocalClassName());
        mActivityStack.remove(HEAD);
        onActivityRemoved(activity);
    }

    public void onActivityRemoved(Activity activity) {
        Log.d(TAG, "Activity removed from the stack: " + activity.getLocalClassName());
    }

    public Activity getLastInstance() {
        Activity activity = null;
        if (!mActivityStack.isEmpty()) {
            activity = mActivityStack.get(HEAD);
        }
        return activity;
    }

    public Stack<Activity> getActivityStack() {
        return mActivityStack;
    }
}
