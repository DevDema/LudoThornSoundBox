package net.ddns.andrewnetwork.ludothornsoundbox.ui.main;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import net.ddns.andrewnetwork.ludothornsoundbox.BuildConfig;
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
        mAdView =  new AdView(this);
        LinearLayout masterLayout = findViewById(R.id.navigation_layout);//findViewById(R.id.adView);
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
        mAdView.setAdUnitId("ca-app-pub-3889032681139142/8666737012");
        mAdView.setAdSize(AdSize.BANNER);

        mInterstitialAd = new InterstitialAd(this);

        mInterstitialAd.setAdUnitId("ca-app-pub-3889032681139142/3864341552");
        mInterstitialAd.setAdListener(adListener);
        AdRequest.Builder adRequestBuilder = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, extras);

        //CONFIGURA TEST DEVICE.
        if(BuildConfig.DEBUG) {
            adRequestBuilder.addTestDevice("E471AADF1D21337710F1244766497DF9");
        }

        mAdView.loadAd(adRequestBuilder.build());
        mInterstitialAd.loadAd(adRequestBuilder.build());

        mAdView.setVisibility(View.GONE);
        masterLayout.addView(mAdView);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);

    }

    public void showInterstitialAd() {
        if(mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }
}
