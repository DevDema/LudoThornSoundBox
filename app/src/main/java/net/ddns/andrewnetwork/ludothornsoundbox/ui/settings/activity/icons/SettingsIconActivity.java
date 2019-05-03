package net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.activity.icons;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.databinding.PreferencesLauncherIconsBinding;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.PreferencesManagerActivity;

import worker8.com.github.radiogroupplus.RadioGroupPlus;

public class SettingsIconActivity extends PreferencesManagerActivity implements RadioGroupPlus.OnCheckedChangeListener {

    public static final int RESULT_ICON_SELECTED = 2014;
    public static final int REQUEST_ICON_SELECTED = 2016;
    public static final int LAUNCHER_ICON_1 = 0;
    public static final int LAUNCHER_ICON_2 = 1;
    public static final int LAUNCHER_ICON_3 = 2;
    public static final String CURRENT_POSITION = "CURRENT_POSITION";
    public static final String EXTRA_ICON_SELECTED = "EXTRA_ICON_SELECTED";
    private PreferencesLauncherIconsBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            mIsHomeButtonEnabled = true;
            mIsDisplayHomeAsUpEnabled = true;
            getSupportActionBar().setHomeButtonEnabled(mIsHomeButtonEnabled);
            getSupportActionBar().setDisplayHomeAsUpEnabled(mIsDisplayHomeAsUpEnabled);
        }

        //STRICTLY IN THIS ORDER.
        checkCurrentPosition();

        mBinding.radioIconGroup.setOnCheckedChangeListener(this);

    }

    private void checkCurrentPosition() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            int currentPosition = getIntent().getExtras().getInt(CURRENT_POSITION);
            switch (currentPosition) {
                default:
                case LAUNCHER_ICON_1:
                    mBinding.radioIconGroup.check(mBinding.button1.getId());
                    break;
                case LAUNCHER_ICON_2:
                    mBinding.radioIconGroup.check(mBinding.button2.getId());
                    break;
                case LAUNCHER_ICON_3:
                    mBinding.radioIconGroup.check(mBinding.button3.getId());
                    break;
            }
        }
    }

    @Override
    protected void setContentView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.preferences_launcher_icons);
    }

    @Override
    protected int getFragmentContainerView() {
        return 0;
    }

    @Override
    public void onCheckedChanged(RadioGroupPlus radioGroupPlus, int i) {
        Intent intent = new Intent();

        if (i == mBinding.button1.getId()) {
            intent.putExtra(EXTRA_ICON_SELECTED, LAUNCHER_ICON_1);
        } else if (i == mBinding.button2.getId()) {
            intent.putExtra(EXTRA_ICON_SELECTED, LAUNCHER_ICON_2);
        } else {
            intent.putExtra(EXTRA_ICON_SELECTED, LAUNCHER_ICON_3);
        }

        setResult(RESULT_OK, intent);

        finish();
    }
}
