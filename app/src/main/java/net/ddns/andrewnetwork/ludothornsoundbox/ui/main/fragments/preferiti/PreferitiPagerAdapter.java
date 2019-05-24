package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.audio.PreferitiAudioFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.video.PreferitiVideoFragment;

public class PreferitiPagerAdapter extends FragmentPagerAdapter {

    private static final int POSITION_AUDIO = 0;
    private static final int POSITION_VIDEO = 1;
    private final Context mContext;

    PreferitiPagerAdapter(Context context, FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case POSITION_AUDIO:
                return PreferitiAudioFragment.newInstance();
            default:
            case POSITION_VIDEO:
                return PreferitiVideoFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case POSITION_AUDIO:
                return mContext.getString(R.string.audio_label);
            case POSITION_VIDEO:
                return mContext.getString(R.string.video);
        }

        return null;
    }
}
