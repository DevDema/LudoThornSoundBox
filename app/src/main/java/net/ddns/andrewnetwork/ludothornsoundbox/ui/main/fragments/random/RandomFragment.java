package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.random;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.databinding.FragmentRandomBinding;
import net.ddns.andrewnetwork.ludothornsoundbox.di.component.ActivityComponent;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.GifFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.random.RandomViewPresenterBinder.IRandomPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.random.RandomViewPresenterBinder.IRandomView;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.AudioUtils;

import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

public class RandomFragment extends GifFragment implements IRandomView {

    private final Random randomGenerator = new Random();
    private FragmentRandomBinding mBinding;
    @Inject
    IRandomPresenter<IRandomView> mPresenter;
    private List<LudoAudio> audioList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_random, container, false);

        ActivityComponent activityComponent = getActivityComponent();
        if(activityComponent != null) {
            activityComponent.inject(this);
            mPresenter.onAttach(this);
        }

        audioList = mPresenter.getAudioListFromPref();

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        mBinding.randomLayout.randombutton.setOnClickListener(view2 -> {

            int index = randomGenerator.nextInt(audioList.size());
            LudoAudio audio = audioList.get(index);

            mBinding.audioPlayer.setAudio(audio);
            mBinding.audioPlayer.play();
        });
    }
}
