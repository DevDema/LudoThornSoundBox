package net.ddns.andrewnetwork.ludothornsoundbox.ui.main;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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
import net.ddns.andrewnetwork.ludothornsoundbox.utils.CommonUtils;

import java.util.function.Function;

public abstract class AdsActivity extends PreferencesManagerActivity {

    public interface AdCustomListener {

        void onAdLoaded();

    }

    public interface AdClosedListener {

        void onAdClosed();
    }

    AdView mAdView;
    InterstitialAd mInterstitialAd;
    AdCustomListener adCustomListener;
    AdClosedListener adClosedListener;
    ImageButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MobileAds.initialize(this, AppConstants.ADS_CODE);

        AdListener adListener = new AdListener() {

            @Override
            public void onAdLoaded() {
                mAdView.setVisibility(View.VISIBLE);

                if (adCustomListener != null) {
                    adCustomListener.onAdLoaded();
                }

                button.setEnabled(true);
            }

            @Override
            public void onAdFailedToLoad(int error) {
                mAdView.setVisibility(View.GONE);

                if (adCustomListener != null) {
                    adCustomListener.onAdLoaded();
                }

                button.setEnabled(false);
            }

            @Override
            public void onAdClosed() {
                AdRequest adRequest = new AdRequest.Builder().build();
                button.setEnabled(false);
                mInterstitialAd.loadAd(adRequest);

                if (adClosedListener != null) {
                    adClosedListener.onAdClosed();
                    adClosedListener = null;
                }
            }

        };

        mInterstitialAd = new InterstitialAd(this);

        mInterstitialAd.setAdUnitId("ca-app-pub-3889032681139142/3864341552");
        mInterstitialAd.setAdListener(adListener);
        Bundle extras = new Bundle();
        AdRequest.Builder adRequestBuilder = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, extras);

        mInterstitialAd.loadAd(adRequestBuilder.build());
        //CONFIGURA TEST DEVICE.
        if (BuildConfig.DEBUG) {
            adRequestBuilder.addTestDevice("E471AADF1D21337710F1244766497DF9");
        }

        LinearLayout masterLayout = getAdRootView();
        if(masterLayout != null) {


            LinearLayout linearLayout = new LinearLayout(this);
            button = new ImageButton(this);

            mAdView = new AdView(this);


            mAdView.setAdListener(adListener);
            mAdView.setAdUnitId("ca-app-pub-3889032681139142/8666737012");
            mAdView.setAdSize(AdSize.BANNER);


            button.setEnabled(false);
            mAdView.loadAd(adRequestBuilder.build());


            mAdView.setVisibility(View.GONE);
            mAdView.setLayoutParams(new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            button.setBackground(ContextCompat.getDrawable(this, R.drawable.block_color_dark));
            button.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_browser_open_white));
            button.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
            button.setOnClickListener(v -> CommonUtils.showDialog(this, getString(R.string.help_message_ad, BuildConfig.SHORT_NAME), (dialog, which) -> showInterstitialAd(null), true));

            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.addView(mAdView);
            linearLayout.addView(button);

            masterLayout.addView(linearLayout);
        }
    }

    protected abstract LinearLayout getAdRootView();

    public void showInterstitialAd(AdClosedListener adClosedListener) {

        this.adClosedListener = adClosedListener;

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else if (adClosedListener != null) {
            adClosedListener.onAdClosed();
        }
    }
}
