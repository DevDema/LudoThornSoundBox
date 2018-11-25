package net.ddns.andrewnetwork.ludothornsoundbox.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.InterstitialAd;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.model.FavoriteAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.model.User;
import net.ddns.andrewnetwork.ludothornsoundbox.view.BaseActivity;

import java.util.ArrayList;
import java.util.Random;

public class RandomFragment extends GifFragment {

    Button randombutton;
    final Random randomGenerator = new Random();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.scroll_random, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view,savedInstanceState);

        randombutton = view.findViewById(R.id.randombutton);

        searchlabel.setVisibility(View.GONE);
        searchstring.setVisibility(View.GONE);
        randombutton.setOnClickListener(view2 -> {
            InterstitialAd interstitialAd = ((BaseActivity) RandomFragment.this.getActivity()).getInterstitialAd();

            if(!User.loadAds(interstitialAd,mediaPlayer)) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }
                int index = randomGenerator.nextInt(favoriteAudioList.size());
                FavoriteAudio audio = favoriteAudioList.get(index);

                mediaPlayer = MediaPlayer.create(getActivity(), audio.getAudio());
                mediaPlayer.setOnCompletionListener(completionListener);

                play_pause.setImageResource(R.drawable.ic_action_pause);
                mediaPlayer.start();

                /*NotificationSender notificationSender = new NotificationSender(RandomActivity.this);
                notificationSender.send("Tasto premuto!");*/

            }});
    }
}
