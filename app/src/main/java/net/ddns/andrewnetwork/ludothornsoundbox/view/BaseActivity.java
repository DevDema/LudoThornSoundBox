package net.ddns.andrewnetwork.ludothornsoundbox.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.model.FavoriteAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.model.User;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity extends AppCompatActivity {

    AdView mAdView;
    InterstitialAd interstitialAd;
    ArrayList<FavoriteAudio> favoriteAudioList = new ArrayList<>();
    final R.raw rawResources = new R.raw();

    float adcounter = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());

        loadAudioList();

        MobileAds.initialize(this, " ca-app-pub-3889032681139142~8858308706");

        /*Premium*/
        if(!User.isPremiumUser())
        {
            mAdView = findViewById(R.id.adView);
            Bundle extras = new Bundle();
            extras.putString("max_ad_content_rating", "T");
            AdRequest adRequest = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, extras).build();
            mAdView.loadAd(adRequest);
            AdListener adListener = new AdListener() {

                @Override
                public void onAdLoaded() {
                    mAdView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAdFailedToLoad(int error) {
                    mAdView.setVisibility(View.GONE);
                }

                @Override
                public void onAdClosed() {
                    AdRequest adRequest = new AdRequest.Builder().build();
                    interstitialAd.loadAd(adRequest);
                }

            };
            mAdView.setAdListener(adListener);
            interstitialAd = new InterstitialAd(this);
            interstitialAd.setAdUnitId("ca-app-pub-3889032681139142/3864341552");
            interstitialAd.setAdListener(adListener);
            interstitialAd.loadAd(new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, extras).build());

        }
    }

    public void loadAudioList() {
        final Class<R.raw> c = R.raw.class;
        final Field[] fields = c.getDeclaredFields();

        for (Field field : fields) {
            String resourcename;
            int resourceid;
            try {
                resourcename = field.getName();
                resourceid = field.getInt(rawResources);
                favoriteAudioList.add(new FavoriteAudio(resourcename,resourceid));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int getDisplayHeight() {
        return getSize().y;
    }

    public int getDisplayWidth() {
        return getSize().x;
    }

    private Point getSize() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public InterstitialAd getInterstitialAd() {
        return interstitialAd;
    }

    public abstract int getContentView();
}
