package net.ddns.andrewnetwork.ludothornsoundbox.ui.base.core.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.core.activities.CoreActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.core.activities.SecondActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.core.extras.FragmentLifeCycleListener;

import java.util.Objects;
import java.util.Stack;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;

/**
 * @author Antonio Cornacchia
 * @version 1.3
 * @edit: Antonio Cornacchia
 * @date: 29/10/2018
 */
public abstract class CoreFragment extends Fragment implements ICoreFragment {

    public static final String TAG = "CoreFragment";

    protected static final int MODE_PRIVATE = Activity.MODE_PRIVATE;
    protected static final int MODE_APPEND = Activity.MODE_APPEND;

    protected static final int RESULT_OK = Activity.RESULT_OK;
    protected static final int RESULT_CANCELED = Activity.RESULT_CANCELED;
    protected CoreActivity mActivity;

    protected boolean mHasOnBackPressed = false;

    protected Stack<FragmentLifeCycleListener> mFragmentLifeCycleListeners;

    protected Stack<OnRequestPermissionsResultCallback> mRequestResults;

    /**
     * The Fragments's saved state
     **/
    protected Bundle mSavedState;

    private Delegate mDelegate = new Delegate(this);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFragmentLifeCycleListeners = new Stack<>();
        if (getActivity() instanceof CoreActivity) {
            mActivity = (CoreActivity) getActivity();
        } else {
            throw new ClassCastException(Objects.requireNonNull(getActivity()).getClass().getSimpleName()
                    + " must extends CoreActivity");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentLifeCycleListeners = new Stack<>();
        mRequestResults = new Stack<>();
    }

    @Override
    public void addLifeCycleListener(FragmentLifeCycleListener flcl, Bundle savedInstanceState) {
        mDelegate.addLifeCycleListener(flcl, savedInstanceState);
    }

    @Override
    public void addOnRequestPermissionsResultListener(OnRequestPermissionsResultCallback listener) {
        mDelegate.addOnRequestPermissionsResultListener(listener);
    }

    @Override
    public void setHasOnBackPressed(boolean hasOnBackPressed) {
        mDelegate.setHasOnBackPressed(hasOnBackPressed);
    }

    @Override
    public boolean hasOnBackPressed() {
        return mDelegate.hasOnBackPressed();
    }

    @Override
    public void onBackPressed() {
        // unused empty method, override to do something
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDelegate.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDelegate.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mDelegate.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mDelegate.onResume();
    }

    @Override
    public void onPause() {
        mDelegate.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        mDelegate.onStop();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mDelegate.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mDelegate.onSaveInstanceState(outState);
    }

    @Override
    public Bundle getSavedInstanceState() {
        return mDelegate.getSavedInstanceState();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mDelegate.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void setSupportActionBar(Toolbar toolbar) {
        mDelegate.setSupportActionBar(toolbar);
    }

    /**
     * Start a fragment into a new Activity to have a result back
     **/
    @Override
    public void startFragmentForResult(Fragment fragment, Class<? extends SecondActivity> cls,
                                       int requestCode) {
        mDelegate.startFragmentForResult(fragment, cls, requestCode);
    }

    /**
     * Start a fragment into a new Activity with an input animation
     **/
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void startFragmentForResult(Fragment fragment, Class<? extends SecondActivity> cls,
                                       int requestCode, ActivityOptionsCompat animation) {
        mDelegate.startFragmentForResult(fragment, cls, requestCode, animation);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
