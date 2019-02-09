package net.ddns.andrewnetwork.ludothornsoundbox.ui.base.core.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.core.activities.CoreActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.core.activities.SecondActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.core.extras.FragmentLifeCycleListener;

import java.util.Objects;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;

import static androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback;

class Delegate implements ICoreFragment, FragmentLifeCycleListener {

    private CoreFragment mFragment;

    Delegate(CoreFragment fragment) {
        mFragment = fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
    }

    @Override
    public void addLifeCycleListener(FragmentLifeCycleListener flcl, Bundle savedInstanceState) {
        mFragment.mSavedState = savedInstanceState;
        mFragment.mFragmentLifeCycleListeners.add(flcl);
        flcl.onCreate(savedInstanceState);
    }

    @Override
    public void addOnRequestPermissionsResultListener(OnRequestPermissionsResultCallback listener) {
        mFragment.mRequestResults.add(listener);
    }

    @Override
    public void setHasOnBackPressed(boolean hasOnBackPressed) {
        mFragment.mHasOnBackPressed = hasOnBackPressed;
    }

    @Override
    public boolean hasOnBackPressed() {
        return mFragment.mHasOnBackPressed;
    }

    @Override
    public void onBackPressed() {
        // unused empty method
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mFragment.mSavedState = savedInstanceState;
        for (FragmentLifeCycleListener listener : mFragment.mFragmentLifeCycleListeners) {
            listener.onViewCreated(view, savedInstanceState);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        mFragment.mSavedState = savedInstanceState;
        for (FragmentLifeCycleListener listener : mFragment.mFragmentLifeCycleListeners) {
            listener.onActivityCreated(savedInstanceState);
        }
    }

    @Override
    public void onStart() {
        for (FragmentLifeCycleListener listener : mFragment.mFragmentLifeCycleListeners) {
            listener.onStart();
        }
    }

    @Override
    public void onResume() {
        for (FragmentLifeCycleListener listener : mFragment.mFragmentLifeCycleListeners) {
            listener.onResume();
        }
    }

    @Override
    public void onPause() {
        for (FragmentLifeCycleListener listener : mFragment.mFragmentLifeCycleListeners) {
            listener.onPause();
        }
    }

    @Override
    public void onStop() {
        for (FragmentLifeCycleListener listener : mFragment.mFragmentLifeCycleListeners) {
            listener.onStop();
        }
    }

    @Override
    public void onDestroy() {
        for (FragmentLifeCycleListener listener : mFragment.mFragmentLifeCycleListeners) {
            listener.onDestroy();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (FragmentLifeCycleListener listener : mFragment.mFragmentLifeCycleListeners) {
            listener.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void setSupportActionBar(Toolbar toolbar) {
        if (mFragment.mActivity.getToolbar() != null) {
            mFragment.mActivity.getToolbar().setVisibility(View.GONE);
        }
        mFragment.mActivity.setSupportActionBar(toolbar);
        Objects.requireNonNull(mFragment.mActivity.getSupportActionBar()).setHomeButtonEnabled(
                mFragment.mActivity.isHomeButtonEnabled());
        mFragment.mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(
                mFragment.mActivity.isDisplayHomeAsUpEnabled());
    }

    @Override
    public void startFragmentForResult(Fragment fragment, Class<? extends SecondActivity> cls,
                                       int requestCode) {
        Bundle extrasArgs = fragment.getArguments();
        Intent intent = new Intent(mFragment.mActivity, cls);
        if (extrasArgs != null) {
            intent.putExtras(extrasArgs);
        }
        intent.putExtra(CoreActivity.INTENT_REQUEST_CODE, requestCode);
        intent.putExtra(CoreActivity.INTENT_FRAGMENT_NAME, fragment.getClass().getName());
        mFragment.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        mFragment.mSavedState = savedState;
    }

    @Override
    public Bundle getSavedInstanceState() {
        return mFragment.mSavedState;
    }

    @Override
    public void startFragmentForResult(Fragment fragment, Class<? extends SecondActivity> cls,
                                       int requestCode, ActivityOptionsCompat animation) {
        Bundle extrasArgs = fragment.getArguments();
        Intent intent = new Intent(mFragment.mActivity, cls);
        if (extrasArgs != null) {
            intent.putExtras(extrasArgs);
        }
        intent.putExtra(CoreActivity.INTENT_REQUEST_CODE, requestCode);
        intent.putExtra(CoreActivity.INTENT_FRAGMENT_NAME, fragment.getClass().getName());
        mFragment.startActivityForResult(intent, requestCode, animation.toBundle());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        for (OnRequestPermissionsResultCallback callback : mFragment.mRequestResults) {
            callback.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
