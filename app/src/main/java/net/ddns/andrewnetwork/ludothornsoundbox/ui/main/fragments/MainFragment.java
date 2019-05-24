package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.BaseFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.MainActivity;

public class MainFragment extends BaseFragment {

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        showHideOptionsMenu();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showHideOptionsMenu();
    }

    private void showHideOptionsMenu() {
        if(mActivity instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) mActivity;
            mainActivity.invalidateOptionsMenu();
        }
    }

    protected  <T extends Fragment> T getFragmentByClass(Class<T> fragmentClass) {
        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            if (fragment.getClass().equals(fragmentClass)) {
                return (T) fragment;
            }
        }

        return null;
    }
}
