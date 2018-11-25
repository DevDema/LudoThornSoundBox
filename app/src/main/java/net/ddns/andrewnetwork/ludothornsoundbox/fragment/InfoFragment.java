package net.ddns.andrewnetwork.ludothornsoundbox.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.view.WebActivity;

public class InfoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.content_credits, container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView telegramludo =  view.findViewById(R.id.telegramtext);
        TextView instagramludo = view.findViewById(R.id.instagramtext);
        TextView facebookludo =  view.findViewById(R.id.facebooktext);
        TextView emailauthor =  view.findViewById(R.id.email);
        TextView youtubeauthor = view.findViewById(R.id.youtube);
        TextView twitterdesigner =  view.findViewById(R.id.twitter);
        TextView instagramdesigner =  view.findViewById(R.id.instagram);
        TextView tipeee = view.findViewById(R.id.tipeee);
        telegramludo.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://telegram.me/LudoThornChannel"));
            startActivity(browserIntent);
        });
        tipeee.setOnClickListener(v -> {
            Intent intent = new Intent(new Intent(getActivity(), WebActivity.class));
            startActivity(intent);
        });
        instagramludo.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.instagram.com/ludothorn/"));
            startActivity(browserIntent);
        });
        facebookludo.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/LudoThorn/"));
            startActivity(browserIntent);
        });
        emailauthor.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("mailto:andrea.de-matteis@outlook.com"));
            startActivity(browserIntent);
        });
        youtubeauthor.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/channel/UCyxgU1CTLrUemNV0kRAqP1A?view_as=subscriber"));
            startActivity(browserIntent);
        });
        twitterdesigner.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://twitter.com/Mawrishi"));
            startActivity(browserIntent);
        });
        instagramdesigner.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.instagram.com/mawri.psd/"));
            startActivity(browserIntent);
        });

    }
}
