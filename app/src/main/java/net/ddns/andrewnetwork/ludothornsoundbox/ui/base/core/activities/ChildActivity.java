package net.ddns.andrewnetwork.ludothornsoundbox.ui.base.core.activities;

import android.os.Bundle;

import net.ddns.andrewnetwork.ludothornsoundbox.R;


/**
 * @author Antonio Cornacchia
 * @version 1.0
 * @date: 06/06/2017
 */

/**
 * Use this class only if you define a parent activity in the manifest
 * **/
public abstract class ChildActivity extends CoreActivity {

    public static final String TAG = "ChildActivity";

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
        launchFragment();
    }

    protected abstract void launchFragment();

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected int getFragmentContainerView() {
        return R.id.content_main;
    }

}
