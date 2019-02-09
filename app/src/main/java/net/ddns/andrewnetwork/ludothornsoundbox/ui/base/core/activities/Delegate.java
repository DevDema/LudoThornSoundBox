package net.ddns.andrewnetwork.ludothornsoundbox.ui.base.core.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.core.application.BaseApplication;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.core.extras.LifeCycleListener;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import static androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback;
import static net.ddns.andrewnetwork.ludothornsoundbox.ui.base.core.activities.CoreActivity.INTENT_FRAGMENT_NAME;
import static net.ddns.andrewnetwork.ludothornsoundbox.ui.base.core.activities.CoreActivity.INTENT_REQUEST_CODE;


class Delegate implements ICoreActivity, LifeCycleListener {

    private static final int HEAD = 0;

    private CoreActivity mActivity;

    Delegate(CoreActivity activity) {
        mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
    }

    @Override
    public void addLifeCycleListener(LifeCycleListener alcl, Bundle savedInstanceState) {
        mActivity.mSavedState = savedInstanceState;
        mActivity.mActivityLifeCycleListeners.add(alcl);
        alcl.onCreate(savedInstanceState);
    }

    @Override
    public void addRequestPermissionsResultListner(OnRequestPermissionsResultCallback listener) {
        mActivity.mRequestResults.add(listener);
    }

    @Override
    public View onCreateView(Bundle savedInstanceState) {
        mActivity.mSavedState = savedInstanceState;
        View root = mActivity.findViewById(android.R.id.content);
        if (root != null) {
            mActivity.mView = ((ViewGroup) root).getChildAt(0);
        }
        mActivity.mAppBarLayout = mActivity.findViewById(R.id.app_bar_layout);
        mActivity.mToolbar = mActivity.findViewById(R.id.toolbar);
        if (mActivity.mToolbar != null) {
            mActivity.setSupportActionBar(mActivity.mToolbar);
        }
        mActivity.mFragmentManager = mActivity.getSupportFragmentManager();
        return root;
    }

    public View getView() {
        return mActivity.mView;
    }

    @Override
    public void onStart() {
        mActivity.mIsRunning = true;
        for (LifeCycleListener listener : mActivity.mActivityLifeCycleListeners) {
            listener.onStart();
        }
    }

    @Override
    public void onResume() {
        for (LifeCycleListener listener : mActivity.mActivityLifeCycleListeners) {
            listener.onResume();
        }
    }

    @Override
    public void onPause() {
        for (LifeCycleListener listener : mActivity.mActivityLifeCycleListeners) {
            listener.onPause();
        }
    }

    @Override
    public void onStop() {
        mActivity.mIsRunning = false;
        for (LifeCycleListener listener : mActivity.mActivityLifeCycleListeners) {
            listener.onStop();
        }
    }

    @Override
    public void onDestroy() {
        for (LifeCycleListener listener : mActivity.mActivityLifeCycleListeners) {
            listener.onDestroy();
        }
        mActivity.mActivityLifeCycleListeners = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (LifeCycleListener listener : mActivity.mActivityLifeCycleListeners) {
            listener.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public AppBarLayout getAppBarLayout() {
        return mActivity.mAppBarLayout;
    }

    @Override
    public Toolbar getToolbar() {
        return mActivity.mToolbar;
    }

    @Override
    public Activity getLastInstance() {
        BaseApplication application = (BaseApplication) mActivity.getApplication();
        return application.getLastActivity();
    }

    @Override
    public Fragment getLastFragment() {
        FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
        return fragmentManager.getFragments().get(HEAD);
    }

    @Override
    public boolean isRunning() {
        return mActivity.mIsRunning;
    }

    @Override
    public boolean isHomeButtonEnabled() {
        return mActivity.mIsHomeButtonEnabled;
    }

    @Override
    public void setHomeButtonEnabled(boolean homeButtonEnabled) {
        mActivity.mIsHomeButtonEnabled = homeButtonEnabled;
    }

    @Override
    public boolean isDisplayHomeAsUpEnabled() {
        return mActivity.mIsDisplayHomeAsUpEnabled;
    }

    @Override
    public void setDisplayHomeAsUpEnabled(boolean displayHomeAsUpEnabled) {
        mActivity.mIsDisplayHomeAsUpEnabled = displayHomeAsUpEnabled;
    }

    @Override
    public void startActivity(Class<? extends Activity> cls) {
        Intent intent = new Intent(mActivity, cls);
        mActivity.startActivity(intent);
    }

    @Override
    public void startFragment(Fragment fragment, Class<? extends SecondActivity> cls) {
        Bundle extrasArgs = fragment.getArguments();
        Intent intent = new Intent(mActivity, cls);
        if (extrasArgs != null) {
            intent.putExtras(extrasArgs);
        }
        intent.putExtra(INTENT_FRAGMENT_NAME, fragment.getClass().getName());
        mActivity.startActivity(intent);
    }

    @Override
    public void startFragmentForResult(Fragment fragment, Class<? extends SecondActivity> cls,
                                       int requestCode) {
        Bundle extrasArgs = fragment.getArguments();
        Intent intent = new Intent(mActivity, cls);
        if (extrasArgs != null) {
            intent.putExtras(extrasArgs);
        }
        intent.putExtra(INTENT_REQUEST_CODE, requestCode);
        intent.putExtra(INTENT_FRAGMENT_NAME, fragment.getClass().getName());
        mActivity.startActivityForResult(intent, requestCode);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void startFragment(Fragment fragment, Class<? extends SecondActivity> cls,
                              ActivityOptionsCompat animation) {
        Bundle extrasArgs = fragment.getArguments();
        Intent intent = new Intent(mActivity, cls);
        if (extrasArgs != null) {
            intent.putExtras(extrasArgs);
        }
        intent.putExtra(INTENT_FRAGMENT_NAME, fragment.getClass().getName());
        mActivity.startActivity(intent, animation.toBundle());

    }

    @Override
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void startFragment(Fragment fragment, Class<? extends SecondActivity> cls, View view, @StringRes int animation) {
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(mActivity, view, mActivity.getString(animation));
        startFragment(fragment, cls, options);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void startFragmentForResult(Fragment fragment, Class<? extends SecondActivity> cls,
                                       int requestCode, ActivityOptionsCompat animation) {
        Bundle extrasArgs = fragment.getArguments();
        Intent intent = new Intent(mActivity, cls);
        if (extrasArgs != null) {
            intent.putExtras(extrasArgs);
        }
        intent.putExtra(INTENT_REQUEST_CODE, requestCode);
        intent.putExtra(INTENT_FRAGMENT_NAME, fragment.getClass().getName());
        mActivity.startActivityForResult(intent, requestCode, animation.toBundle());

    }

    @Override
    public int getRequestCode() {
        int requestCode = -1;
        Intent intent = mActivity.getIntent();
        if (intent != null) {
            requestCode = intent.getIntExtra(INTENT_REQUEST_CODE, -1);
        }
        return requestCode;
    }

    @Override
    public void addFragment(Fragment fragment) {
        if (mActivity.mFragmentsNumber != 0) {
            mActivity.mFragmentManager.beginTransaction().add(mActivity.getFragmentContainerView(),
                    fragment).addToBackStack(fragment.getClass().getName()).commit();
            mActivity.mFragmentsNumber++;
        } else {
            replaceFragment(fragment);
        }
    }

    @Override
    public void replaceFragment(Fragment fragment) {
        mActivity.mFragmentsNumber = 1 + getBackStackCount();
        mActivity.mFragmentManager.beginTransaction().replace(mActivity.getFragmentContainerView(),
                fragment).commit();
    }

    @Override
    public int getBackStackCount() {
        return mActivity.mFragmentManager.getBackStackEntryCount();
    }

    @Override
    public int getFragmentsNumber() {
        return mActivity.mFragmentsNumber;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        for (OnRequestPermissionsResultCallback callback : mActivity.mRequestResults) {
            callback.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void showToastLong(int resString) {
        Toast.makeText(mActivity, resString, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showToastShort(int resString) {
        Toast.makeText(mActivity, resString, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDialog(Dialog dialog) {
        dialog.setOwnerActivity(mActivity);
        dialog.show();
    }

    @Override
    public void showAlertDialog(int resTitle, int resMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(resTitle);
        builder.setMessage(resMessage);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ok, (dialogInterface, i) -> dialogInterface.dismiss());
        builder.show();
    }

    @Override
    public AlertDialog.Builder buildAlertDialog(int resTitle, int resMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(resTitle);
        builder.setMessage(resMessage);
        builder.setCancelable(false);
        return builder;
    }

    @Override
    public void showSnackbar(int resTitle) {
        buildSnackbar(resTitle).show();
    }

    @Override
    public Snackbar buildSnackbar(int resTitle) {
        return Snackbar.make(getView(), resTitle, Snackbar.LENGTH_LONG);
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        mActivity.mSavedState = savedState;
    }

    @Override
    public Bundle getSavedInstanceState() {
        return mActivity.mSavedState;
    }

}
