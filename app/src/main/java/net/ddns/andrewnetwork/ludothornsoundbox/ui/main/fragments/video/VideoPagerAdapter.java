package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import net.ddns.andrewnetwork.ludothornsoundbox.data.model.Channel;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.child.ChildVideoFragment;

import java.util.List;

public class VideoPagerAdapter extends FragmentPagerAdapter {

    private final List<Channel> channelList;

    VideoPagerAdapter(FragmentManager fm, List<Channel> channelList) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        this.channelList = channelList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return ChildVideoFragment.newInstance(channelList.get(position));
    }

    @Override
    public int getCount() {
        return channelList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return channelList.get(position).getChannelName();
    }

}
