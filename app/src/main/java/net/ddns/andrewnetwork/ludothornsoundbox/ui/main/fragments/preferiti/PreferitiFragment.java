package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.databinding.FragmentFavoriteBinding;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.MainFragment;

public class PreferitiFragment extends MainFragment {

    private FragmentFavoriteBinding mBinding;

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

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PreferitiPagerAdapter adapter = new PreferitiPagerAdapter(mContext, getChildFragmentManager());

        mBinding.viewPager.setAdapter(adapter);

        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
    }
}
