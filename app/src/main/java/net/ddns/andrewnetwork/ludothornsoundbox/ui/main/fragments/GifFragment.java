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
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.utils.DataSingleTon;

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
        gif = view.findViewById(R.id.gif);

        gif.setOnClickListener(view2 -> {
            Uri uri = Uri.parse("https://www.youtube.com/user/LudoThornDoppiaggio");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
    }

}
