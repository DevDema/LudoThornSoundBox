package net.ddns.andrewnetwork.ludothornsoundbox.ui.social;

import android.os.Bundle;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.PreferencesManagerActivity;

public class SocialActivity extends PreferencesManagerActivity {

    public static final int REQUEST_SOCIAL = 4044;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            mIsHomeButtonEnabled = true;
            mIsDisplayHomeAsUpEnabled = true;
            getSupportActionBar().setHomeButtonEnabled(mIsHomeButtonEnabled);
            getSupportActionBar().setDisplayHomeAsUpEnabled(mIsDisplayHomeAsUpEnabled);
        }
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_social);
    }

    @Override
    protected int getFragmentContainerView() {
        return 0;
    }
}
