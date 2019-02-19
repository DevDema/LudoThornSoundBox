package net.ddns.andrewnetwork.ludothornsoundbox.ui.main;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.BaseActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.AppConstants;

import java.lang.reflect.Field;
import java.util.ArrayList;

public abstract class ParentActivity extends BaseActivity {

    public interface AdCustomListener {

        void onAdLoaded();
    }
    AdView mAdView;
    InterstitialAd interstitialAd;
    AdCustomListener adCustomListener;

    float adcounter = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        MobileAds.initialize(this, AppConstants.ADS_CODE);

            mAdView = findViewById(R.id.adView);
            Bundle extras = new Bundle();
            //extras.putString("max_ad_content_rating", "T");
            AdListener adListener = new AdListener() {

                @Override
                public void onAdLoaded() {
                    mAdView.setVisibility(View.VISIBLE);
                    if(adCustomListener != null)
                        adCustomListener.onAdLoaded();
                }

                @Override
                public void onAdFailedToLoad(int error) {
                    mAdView.setVisibility(View.GONE);
                    if(adCustomListener != null)
                        adCustomListener.onAdLoaded();
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
            mAdView.loadAd(new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, extras).build());
            interstitialAd.loadAd(new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, extras).build());
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

}
