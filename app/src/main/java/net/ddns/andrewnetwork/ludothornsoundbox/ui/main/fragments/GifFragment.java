package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import net.ddns.andrewnetwork.ludothornsoundbox.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import pl.droidsonroids.gif.GifTextView;

public abstract class GifFragment extends ParentFragment {

    GifTextView gif;
    ImageView stop;
    EditText searchstring;
    TextView searchlabel;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_gif, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        play_pause = view.findViewById(R.id.play);
        stop = view.findViewById(R.id.stop);
        play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    play_pause.setImageResource(R.drawable.ic_play_white);
                }
                else {
                    mediaPlayer.start();
                    play_pause.setImageResource(R.drawable.ic_pause_white);

                }
            }
        });
        stop.setOnClickListener(view2 -> {
            if(mediaPlayer.isPlaying())
                mediaPlayer.stop();
            play_pause.setImageResource(R.drawable.ic_play_white);
        });
        gif = view.findViewById(R.id.gif);

        gif.setOnClickListener(view2 -> {
            Uri uri = Uri.parse("https://www.youtube.com/user/LudoThornDoppiaggio");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        searchstring = view.findViewById(R.id.searchstring);
        searchlabel = view.findViewById(R.id.searchlabel);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(completionListener);
    }
}
