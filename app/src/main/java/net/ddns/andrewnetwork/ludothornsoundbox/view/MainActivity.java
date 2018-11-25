package net.ddns.andrewnetwork.ludothornsoundbox.view;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.controller.passVideoList;
import net.ddns.andrewnetwork.ludothornsoundbox.fragment.FavoriteFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.fragment.HomeFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.fragment.InfoFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.fragment.RandomFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.fragment.VideoFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.model.Channel;
import net.ddns.andrewnetwork.ludothornsoundbox.model.LudoVideo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends BaseActivity implements passVideoList {
    ArrayList<Channel> channels;
    int executionNumberVideoFragment=0;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        Bundle bundle = new Bundle();
        bundle.putInt("width",getDisplayWidth());
        Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.action_home:
                        bundle.putSerializable("favoriteAudioList",favoriteAudioList);
                        fragment = new HomeFragment();
                        break;
                    case R.id.action_random:
                        bundle.putSerializable("favoriteAudioList",favoriteAudioList);
                        fragment = new RandomFragment();
                        break;
                    case R.id.action_video:
                        if(channels!=null && !channels.isEmpty()) bundle.putSerializable("channelList",channels);
                        if(executionNumberVideoFragment!=0) bundle.putInt("executionNumber", executionNumberVideoFragment);
                        fragment = new VideoFragment();
                        break;
                    case R.id.action_favorites:
                        bundle.putInt("height",getDisplayHeight());
                        fragment = new FavoriteFragment();
                        break;
                    case R.id.action_info:
                        fragment = new InfoFragment();
                        break;
                }
                bundle.putFloat("adcounter",adcounter);
                Objects.requireNonNull(fragment).setArguments(bundle);
                return loadFragment(fragment);
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Bundle bundle = new Bundle();
        bundle.putInt("width", getDisplayWidth());
        if (savedInstanceState == null) {

            Fragment fragment = new HomeFragment();
            bundle.putSerializable("favoriteAudioList", favoriteAudioList);
            bundle.putFloat("adcounter", adcounter);
            fragment.setArguments(bundle);
            loadFragment(fragment);
        }
        else if(executionNumberVideoFragment!=0) bundle.putInt("executionNumber", executionNumberVideoFragment);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_mainfragments;

    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }


    @Override
    public void onVideoListReceived(ArrayList<Channel> channels) {
        this.channels= new ArrayList<>(channels);
    }

    @Override
    public void OnExecutionNumberReceived(int executionNumberVideoFragment) {
        this.executionNumberVideoFragment= executionNumberVideoFragment;
    }
}
