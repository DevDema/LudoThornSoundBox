package net.ddns.andrewnetwork.ludothornsoundbox.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.ads.InterstitialAd;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.model.FavoriteAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.model.User;
import net.ddns.andrewnetwork.ludothornsoundbox.view.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public abstract class ParentFragment extends Fragment {
    Bundle bundle;
    MediaPlayer mediaPlayer;
    ImageView play_pause;
    Button.OnClickListener playAudio;

    MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {

        public void onCompletion(MediaPlayer mp) {

            play_pause.setImageResource(R.drawable.ic_action_play);
            ParentFragment.this.mediaPlayer.setOnCompletionListener(this);
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

    public boolean isTablet() {
        int width = bundle.getInt("width");
        if(width > 1500) return true;
        return false;
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
