package net.ddns.andrewnetwork.ludothornsoundbox.ui.settings;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.PreferencesManagerActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.fragments.SettingsFragment;

import static net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.activity.navigationItems.SettingsNavigationItemsActivity.KEY_CURRENT_POSITION_BOT_NAV_MENU;

public class SettingsActivity extends PreferencesManagerActivity implements PreferenceFragmentCompat.OnPreferenceStartScreenCallback {

    public static final int REQUEST_SETTINGS_CHANGE = 2022;
    int currentNavigationItemPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            mIsHomeButtonEnabled = true;
            mIsDisplayHomeAsUpEnabled = true;
            getSupportActionBar().setHomeButtonEnabled(mIsHomeButtonEnabled);
            getSupportActionBar().setDisplayHomeAsUpEnabled(mIsDisplayHomeAsUpEnabled);
        }

        if (getIntent() != null && getIntent().getExtras() != null) {
            currentNavigationItemPosition = getIntent().getExtras().getInt(KEY_CURRENT_POSITION_BOT_NAV_MENU);
        }

        replaceFragment(SettingsFragment.newInstance(currentNavigationItemPosition));
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

    @Override
    public boolean onPreferenceStartScreen(PreferenceFragmentCompat caller, PreferenceScreen preferenceScreen) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(PreferenceFragmentCompat.ARG_PREFERENCE_ROOT, preferenceScreen.getKey());
        fragment.setArguments(args);
        ft.replace(getFragmentContainerView(), fragment, preferenceScreen.getKey());
        ft.addToBackStack(preferenceScreen.getKey());
        ft.commit();

        return true;
    }
}
