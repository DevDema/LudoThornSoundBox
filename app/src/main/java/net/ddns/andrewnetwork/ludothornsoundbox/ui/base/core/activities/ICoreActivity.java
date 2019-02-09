package net.ddns.andrewnetwork.ludothornsoundbox.ui.base.core.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;

import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.core.extras.LifeCycleListener;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;

/**
 * @author Antonio Cornacchia
 * @version 1.0
 * @date: 20/01/2017
 **/
interface ICoreActivity {

    /**
     * Add a listener on the mActivity's lifecycle
     **/
    void addLifeCycleListener(LifeCycleListener alcl, Bundle savedInstanceState);

    /**
     * Add a listener on the mActivity's lifecycle
     **/
    void addRequestPermissionsResultListner(OnRequestPermissionsResultCallback listener);

    /**
     * Return the layout's root view after its creation
     **/
    View onCreateView(Bundle savedInstanceState);

    /**
     * Return the layout's root view
     **/
    View getView();

    /**
     * Return the toolbar's container
     **/
    AppBarLayout getAppBarLayout();

    /**
     * Return the mActivity's toolbar
     **/
    Toolbar getToolbar();

    /**
     * Return the latest instantiated mActivity
     **/
    Activity getLastInstance();

    /**
     * Return the latest instantiated fragment (on container view)
     **/
    Fragment getLastFragment();

    /**
     * Return true if the mActivity is in foreground, false otherwise
     **/
    boolean isRunning();

    /**
     * Make the home icon clickable, with the color at the background of the icon as a feedback
     * of the click
     **/
    void setHomeButtonEnabled(boolean homeButtonEnabled);

    /**
     * Return true if the home button (hamburger or arrow) is enabled
     **/
    boolean isHomeButtonEnabled();

    /**
     * Make the home icon clickable and add the arrow (back button)
     **/
    void setDisplayHomeAsUpEnabled(boolean displayHomeAsUpEnabled);

    /**
     * Return true if the home button (arrow) is enabled
     **/
    boolean isDisplayHomeAsUpEnabled();

    /**
     * Create back stack when starting the mActivity
     **/
    void startActivity(Class<? extends Activity> cls);

    /**
     * Start a fragment into a new Activity
     **/
    void startFragment(Fragment fragment, Class<? extends SecondActivity> cls);

    /**
     * Start a fragment into a new Activity to have a result back
     **/
    void startFragmentForResult(Fragment fragment, Class<? extends SecondActivity> cls,
                                int requestCode);

    /**
     * Start a fragment into a new Activity with an input animation
     **/
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    void startFragment(Fragment fragment, Class<? extends SecondActivity> cls,
                       ActivityOptionsCompat animation);

    /**
     * Start a fragment into a new Activity with an input animation
     **/
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    void startFragment(Fragment fragment, Class<? extends SecondActivity> cls,
                       View view, @StringRes int animation);

    /**
     * Start a fragment into a new Activity with an input animation
     **/
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    void startFragmentForResult(Fragment fragment, Class<? extends SecondActivity> cls,
                                int requestCode, ActivityOptionsCompat animation);

    int getRequestCode();

    /**
     * Add a new fragment in the fragment stack
     **/
    void addFragment(Fragment fragment);

    /**
     * Replace the current fragment with a new one
     **/
    void replaceFragment(Fragment fragment);

    /**
     * Return the number of fragments in the backstack
     **/
    int getBackStackCount();

    /**
     * Return the number of fragments in the backstack
     **/
    int getFragmentsNumber();

    /**
     * Called after user decides if grant result or not
     **/
    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);

    /**
     * Show a message in a toast view in long time
     **/
    void showToastLong(int resString);

    /**
     * Show a message in a toast view in short time
     **/
    void showToastShort(int resString);

    /**
     * Show the input dialog
     **/
    void showDialog(Dialog dialog);

    /**
     * Show a simple message dialog
     **/
    void showAlertDialog(int resTitle, int resMessage);

    /**
     * Build and get a simple customizable Alert Dialog
     **/
    AlertDialog.Builder buildAlertDialog(int resTitle, int resMessage);

    /**
     * Show a simple Snackbar
     **/
    void showSnackbar(int resTitle);

    /**
     * Build and get a simple customizable Snackbar
     **/
    Snackbar buildSnackbar(int resTitle);

    /**
     * Called when the activity goes on saved instance
     **/
    void onSaveInstanceState(Bundle savedState);

    /**
     * Get the activity instance saved state
     **/
    Bundle getSavedInstanceState();
}
