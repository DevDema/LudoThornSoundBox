package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.databinding.FragmentFavoriteBinding;
import net.ddns.andrewnetwork.ludothornsoundbox.di.component.ActivityComponent;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.MvpView;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.MainFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.PreferitiMainViewPresenterBinder.IPreferitiMainPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.audio.PreferitiAudioFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.video.PreferitiVideoFragment;

import javax.inject.Inject;

import static net.ddns.andrewnetwork.ludothornsoundbox.data.prefs.AppPreferencesHelper.PREF_KEY_PREFERITI_AUDIO;
import static net.ddns.andrewnetwork.ludothornsoundbox.data.prefs.AppPreferencesHelper.PREF_KEY_PREFERITI_VIDEO;

public class PreferitiFragment extends MainFragment implements IAudioVideoAdaptersBinder, SharedPreferences.OnSharedPreferenceChangeListener {

    private FragmentFavoriteBinding mBinding;
    @Inject
    IPreferitiMainPresenter<MvpView> mPresenter;
    public static PreferitiFragment newInstance() {

        Bundle args = new Bundle();

        PreferitiFragment fragment = new PreferitiFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false);

        ActivityComponent activityComponent = getActivityComponent();

        if(activityComponent != null) {
            activityComponent.inject(this);
            mPresenter.onAttach(this);
        }

        mPresenter.registerOnSharedPreferencesChangeListener(this);

        return mBinding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mPresenter.unregisterOnSharedPreferencesChangeListener(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PreferitiPagerAdapter adapter = new PreferitiPagerAdapter(mContext, getChildFragmentManager());

        mBinding.viewPager.setAdapter(adapter);

        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        notifyChildrenHiddenChanged(hidden);
    }

    private void notifyChildrenHiddenChanged(boolean hidden) {
        for(Fragment fragment : getChildFragmentManager().getFragments()) {
            if(fragment instanceof ChildPreferitiFragment) {
                ((ChildPreferitiFragment) fragment).onParentHiddenChanged(hidden);
            }
        }
    }

    @Override
    public void notifySettingPreferito(Class<? extends PreferitiListAdapter> tClass, int position, boolean isSettingPreferito) {
        for(Fragment fragment : getChildFragmentManager().getFragments()) {
            if(fragment instanceof ChildPreferitiFragment) {
                ((ChildPreferitiFragment) fragment).notifySettingPreferito(tClass, position, isSettingPreferito);
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case PREF_KEY_PREFERITI_AUDIO:
                PreferitiAudioFragment audioFragment = getFragmentByClass(PreferitiAudioFragment.class);
                audioFragment.onPreferenceChanged(sharedPreferences);
                break;
            case PREF_KEY_PREFERITI_VIDEO:
                PreferitiVideoFragment videoFragment = getFragmentByClass(PreferitiVideoFragment.class);
                videoFragment.onPreferenceChanged(sharedPreferences);
                break;
        }
    }
}
