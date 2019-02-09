package net.ddns.andrewnetwork.ludothornsoundbox.ui.base.core.fragments;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;

import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.core.activities.SecondActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.core.extras.FragmentLifeCycleListener;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;

import static androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback;

public interface ICoreFragment {

    /**
     * Add a listener on the fragment's lifecycle
     **/
    void addLifeCycleListener(FragmentLifeCycleListener flcl, Bundle savedInstanceState);

    /**
     * Add a listener on request permission result
     **/
    void addOnRequestPermissionsResultListener(OnRequestPermissionsResultCallback listener);

    /**
     * Set the toolbar on back pressed to fragment's implementation
     **/
    void setHasOnBackPressed(boolean hasOnBackPressed);

    /**
     * Return true if the fragment implements the onBackPressed() method
     **/
    boolean hasOnBackPressed();

    /**
     * The method called when on back button is clicked
     **/
    void onBackPressed();

    /**
     * Set the toolbar fragment as the main mActivity toolbar
     **/
    void setSupportActionBar(Toolbar toolbar);

    /**
     * Start a fragment into a new Activity to have a result back
     **/
    void startFragmentForResult(Fragment fragment, Class<? extends SecondActivity> cls,
                                int requestCode);

    /**
     * Called when the activity goes on saved instance
     **/
    void onSaveInstanceState(Bundle savedState);

    /**
     * Get the activity instance saved state
     **/
    Bundle getSavedInstanceState();

    /**
     * Start a fragment into a new Activity with an input animation
     **/
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    void startFragmentForResult(Fragment fragment, Class<? extends SecondActivity> cls,
                                int requestCode, ActivityOptionsCompat animation);

    /**
     * Called after user decides if grant result or not
     **/
    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);

}
