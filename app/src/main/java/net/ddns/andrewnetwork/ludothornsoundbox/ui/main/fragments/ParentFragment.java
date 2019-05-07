package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.BaseFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.CommonUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.mContext = context;
    }

    @NonNull
    @Override
    public Context getContext() {
        return super.getContext() != null ? super.getContext() : mContext;
    }


}

