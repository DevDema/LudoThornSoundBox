package net.ddns.andrewnetwork.ludothornsoundbox.ui.base.core.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import net.ddns.andrewnetwork.ludothornsoundbox.R;

import androidx.fragment.app.Fragment;

/**
 * @author Antonio Cornacchia
 * @version 1.2
 * @date: 19/07/2016
 */
public class SecondActivity extends CoreActivity {

    public static final String TAG = "SecondActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // For back arrow (home) button
        if (getSupportActionBar() != null) {
            mIsHomeButtonEnabled = true;
            mIsDisplayHomeAsUpEnabled = true;
            getSupportActionBar().setHomeButtonEnabled(mIsHomeButtonEnabled);
            getSupportActionBar().setDisplayHomeAsUpEnabled(mIsDisplayHomeAsUpEnabled);
        }

        // Get the fragment name
        Intent intent = getIntent();
        String fragmentName = intent.getStringExtra(INTENT_FRAGMENT_NAME);
        Log.d(TAG, "Class type found: " + fragmentName);

        try {
            // Find a class with the name already found
            Class<?> cls = Class.forName(fragmentName);
            // Create a new instance for the fragment
            Fragment fragment = (Fragment) cls.newInstance();
            // Remove temporally the fragment name from the intent bundle
            intent.removeExtra(INTENT_FRAGMENT_NAME);
            // Check if there are some bundle infos
            if(intent.getExtras()!= null && !intent.getExtras().isEmpty()){
            // Now set the intent extras as fragment arguments
                fragment.setArguments(intent.getExtras());
            }
            // Launch the fragment
            replaceFragment(fragment);
            // Insert the fragment name to recover the fragment in any moment
            intent.putExtra(INTENT_FRAGMENT_NAME, fragmentName);
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "ClassNotFoundException: " + e.getMessage());
        } catch (InstantiationException e) {
            Log.e(TAG, "InstantiationException: " + e.getMessage());
        } catch (IllegalAccessException e) {
            Log.e(TAG, "IllegalAccessException: " + e.getMessage());
        }
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.app_bar_main);
    }

    @Override
    protected int getFragmentContainerView() {
        return R.id.content_main;
    }
}
