package net.ddns.andrewnetwork.ludothornsoundbox.ui.main;

import android.os.Bundle;
import android.view.View;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.PreferencesManagerActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.AppConstants;

public abstract class ParentActivity extends PreferencesManagerActivity {

    public interface AdCustomListener {

        void onAdLoaded();

    }

    AdView mAdView;
    InterstitialAd mInterstitialAd;
    AdCustomListener adCustomListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        MobileAds.initialize(this, AppConstants.ADS_CODE);

        mAdView = findViewById(R.id.adView);
        Bundle extras = new Bundle();

        AdListener adListener = new AdListener() {

            @Override
            public void onAdLoaded() {
                mAdView.setVisibility(View.VISIBLE);

                if (adCustomListener != null) {
                    adCustomListener.onAdLoaded();
                }
            }

            @Override
            public void onAdFailedToLoad(int error) {
                mAdView.setVisibility(View.GONE);

                if (adCustomListener != null) {
                    adCustomListener.onAdLoaded();
                }
            }

            @Override
            public void onAdClosed() {
                AdRequest adRequest = new AdRequest.Builder().build();
                mInterstitialAd.loadAd(adRequest);
            }

        };
        mAdView.setAdListener(adListener);
        mInterstitialAd = new InterstitialAd(this);

        mInterstitialAd.setAdUnitId("ca-app-pub-3889032681139142/3864341552");
        mInterstitialAd.setAdListener(adListener);
        mAdView.loadAd(new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, extras).build());
        mInterstitialAd.loadAd(new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, extras).build());

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);

    }
}
