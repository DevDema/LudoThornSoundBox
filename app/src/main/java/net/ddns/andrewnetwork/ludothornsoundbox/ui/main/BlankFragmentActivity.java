package net.ddns.andrewnetwork.ludothornsoundbox.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.IdRes;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.databinding.ActivityBlankFragmentBinding;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.PreferencesManagerActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.HomeFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.PreferitiFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.random.RandomFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.VideoFragment;

public class BlankFragmentActivity extends PreferencesManagerActivity {

    static final String KEY_EXTRA_FRAGMENT_ACTION = "KEY_EXTRA_FRAGMENT_ACTION";
    static final int REQUEST_BLANK_ACTIVITY = 2000;

    private ActivityBlankFragmentBinding mBinding;
    private @IdRes int actionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getIntent() != null && getIntent().getExtras() != null) {
            actionId = getIntent().getExtras().getInt(KEY_EXTRA_FRAGMENT_ACTION);
        } else {
            Log.e("ActionID", "Action ID is 0");
            finish();
        }

        if (getSupportActionBar() != null) {
            mIsHomeButtonEnabled = true;
            mIsDisplayHomeAsUpEnabled = true;
            getSupportActionBar().setHomeButtonEnabled(mIsHomeButtonEnabled);
            getSupportActionBar().setDisplayHomeAsUpEnabled(mIsDisplayHomeAsUpEnabled);


            getSupportActionBar().setTitle(getStringById(this, actionId));

        }

        replaceFragment(getFragmentById(actionId));
    }

    private static String getStringById(Context context, @IdRes int actionId) {
        switch (actionId) {
            default:
            case R.id.action_home:
                return context.getString(R.string.home);
            case R.id.action_favorites:
                return context.getString(R.string.favorites);
            case R.id.action_random:
                return context.getString(R.string.random);
            case R.id.action_video:
                return context.getString(R.string.video);
        }
    }

    private Fragment getFragmentById(@IdRes int actionId) {
        switch (actionId) {
            default:
            case R.id.action_home:
                return HomeFragment.newInstance(loadAtOnce);
            case R.id.action_favorites:
                return PreferitiFragment.newInstance();
            case R.id.action_random:
                return RandomFragment.newInstance();
            case R.id.action_video:
                return VideoFragment.newInstance();
        }
    }
    @Override
    protected void setContentView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_blank_fragment);
    }

    @Override
    protected int getFragmentContainerView() {
        return R.id.container_fragment;
    }
}
