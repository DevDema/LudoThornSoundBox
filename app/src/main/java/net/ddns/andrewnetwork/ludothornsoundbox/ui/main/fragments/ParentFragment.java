package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments;

import android.content.res.Configuration;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.BaseFragment;

public abstract class ParentFragment extends BaseFragment {
    Bundle bundle;
    protected ImageView play_pause;

    protected MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {

        public void onCompletion(MediaPlayer mp) {

            play_pause.setImageResource(R.drawable.ic_play_white);
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bundle = getArguments();
    }


    public boolean isHorizontal() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }


    protected int getActivityWidth() {
        Point size = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(size);        // Checks the orientation of the screen
        return size.x;
    }

    protected int getActivityHeight() {
        Point size = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(size);        // Checks the orientation of the screen
        return size.y;
    }

    //                                  DECOMMENTA SE VUOI INTERROMPERE I VIDEO QUANDO CAMBI I FRAGMENT
   /* @Override
    public void onDetach() {
        super.onDetach();

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }*/
}

