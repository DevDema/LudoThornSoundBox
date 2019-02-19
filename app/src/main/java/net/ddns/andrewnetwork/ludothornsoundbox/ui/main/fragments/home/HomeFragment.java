package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.databinding.FragmentHomeBinding;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.GifFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.view.ButtonViewPagerAdapter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.view.OnButtonSelectedListener;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.utils.DataSingleTon;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.AudioUtils;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.SpinnerUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

public class HomeFragment extends GifFragment implements OnButtonSelectedListener<LudoAudio> {

    private FragmentHomeBinding mBinding;
    private List<LudoAudio> audioList;
    private MediaPlayer mediaPlayer;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.spinner.setAdapter(SpinnerUtils.createOrderAdapter(getContext()));

        mBinding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ButtonViewPagerAdapter<LudoAudio> adapter = new ButtonViewPagerAdapter<>(getContext(), audioList, LudoAudio::getTitle);
        mBinding.buttonsAudioPager.setAdapter(adapter);

        adapter.setOnButtonSelectedListener(this);

        mBinding.buttonRight.setOnClickListener(v -> mBinding.buttonsAudioPager.arrowScroll(View.FOCUS_RIGHT));
        mBinding.buttonLeft.setOnClickListener(v -> mBinding.buttonsAudioPager.arrowScroll(View.FOCUS_LEFT));

        mBinding.audioHeader.searchstring.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        audioList = AudioUtils.createAudioList();
        mediaPlayer =  DataSingleTon.getInstance().getMediaPlayer();
    }

    @Override
    public void onButtonSelected(LudoAudio audio, int position, Button button) {
        AudioUtils.playTrack(getContext(), mediaPlayer, audio);
    }
}
