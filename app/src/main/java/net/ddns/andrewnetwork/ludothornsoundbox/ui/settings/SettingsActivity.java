package net.ddns.andrewnetwork.ludothornsoundbox.ui.settings;

import android.os.Bundle;

import androidx.annotation.Nullable;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.PreferencesManagerActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.fragments.SettingsFragment;

public class SettingsActivity extends PreferencesManagerActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        replaceFragment(SettingsFragment.newInstance());

        if (getSupportActionBar() != null) {
            mIsHomeButtonEnabled = true;
            mIsDisplayHomeAsUpEnabled = true;
            getSupportActionBar().setHomeButtonEnabled(mIsHomeButtonEnabled);
            getSupportActionBar().setDisplayHomeAsUpEnabled(mIsDisplayHomeAsUpEnabled);
        }
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_settings);
    }

    @Override
    protected int getFragmentContainerView() {
        return R.id.content_settings_fragment;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
