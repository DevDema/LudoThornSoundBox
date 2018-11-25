package net.ddns.andrewnetwork.ludothornsoundbox.fragment;

import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.InterstitialAd;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.controller.FavoriteController;
import net.ddns.andrewnetwork.ludothornsoundbox.model.FavoriteAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.model.User;
import net.ddns.andrewnetwork.ludothornsoundbox.view.BaseActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.view.MainActivity;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static net.ddns.andrewnetwork.ludothornsoundbox.controller.FavoriteController.deleteFavorite;

public class FavoriteFragment extends ParentFragment {


    List<FavoriteAudio> audios = new ArrayList<>();
    int height;
    Typeface typeFace;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.content_favorite, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        height = bundle.getInt("height");
        createList(view);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        typeFace = Typeface.createFromAsset(getActivity().getAssets(), "font/knewave.ttf");

    }

    private void createList(View view) {
        LinearLayout linearLayout = view.findViewById(R.id.linear);
        TextView nofavorite = view.findViewById(R.id.nofavorite);
        TextView deletefavorite = view.findViewById(R.id.deletefavorite);
        try {
            audios = FavoriteController.loadFavorite(getActivity());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (audios != null && !audios.isEmpty()) {

            nofavorite.setVisibility(View.GONE);
            for (FavoriteAudio audio : audios) {
                Button button = new Button(getActivity());
                button.setText(audio.getTitle());
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                ViewGroup.MarginLayoutParams params2 = new ViewGroup.MarginLayoutParams(params);
                params2.setMargins(50,10,50,10);
                button.setLayoutParams(params2);
                button.setOnClickListener(v -> {

                    InterstitialAd interstitialAd = ((BaseActivity) FavoriteFragment.this.getActivity()).getInterstitialAd();

                    if(!User.loadAds(interstitialAd, mediaPlayer)) {
                        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                        }
                        int audionumber = audio.getAudio();
                        if(FavoriteController.isResourceIdInPackage(getActivity(), audionumber)) {
                            mediaPlayer = MediaPlayer.create(getActivity(), audionumber);
                            if(mediaPlayer != null)mediaPlayer.start();
                            else Toast.makeText(getActivity(),"Oops! C'è stato un errore con la riproduzione! Prova ad eliminare e riaggiugere l'audio ",Toast.LENGTH_LONG).show();
                        }
                        else Toast.makeText(getActivity(),"Oops! C'è stato un errore con la riproduzione! Prova ad eliminare e riaggiugere l'audio ",Toast.LENGTH_LONG).show();
                    }});
                button.setOnLongClickListener(view2 -> {
                    if(deleteFavorite(getActivity(), audio))
                    {
                        audios.remove(audio);

                        button.setVisibility(View.GONE);
                        if(audios.size() == 0)
                        {
                            nofavorite.setVisibility(View.VISIBLE);
                            deletefavorite.setVisibility(View.GONE);
                        }
                        Toast.makeText(getActivity(), "Audio rimosso dai preferiti!", Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(getActivity(), "Oops! C'è stato un errore", Toast.LENGTH_SHORT).show();
                    return true;
                });
                button.setMaxLines(2);
                button.setTypeface(typeFace);
                button.setVisibility(View.VISIBLE);
                button.setHeight(height/9);
                button.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.white), PorterDuff.Mode.MULTIPLY);
                button.setGravity(Gravity.CENTER_VERTICAL);
                linearLayout.addView(button);
            }
        }
        else deletefavorite.setVisibility(View.GONE);
    }
}
