package net.ddns.andrewnetwork.ludothornsoundbox.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
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

import java.util.ArrayList;
import java.util.List;

public abstract class AdsActivity extends PreferencesManagerActivity {

    protected LinearLayout bottomBanner;

    public interface AdClosedListener {

        void onAdClosed();
    }

    private List<AdView> mAdViews = new ArrayList<>();
    private InterstitialAd mInterstitialAd;
    private AdClosedListener adClosedListener;
    private ImageButton button;
    private int[] adsBannerTrial;
    private String[] adsBannerUnitId = {"ca-app-pub-3889032681139142/8666737012", "ca-app-pub-3889032681139142/8666737012", "ca-app-pub-3889032681139142/8666737012"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MobileAds.initialize(this, AppConstants.ADS_CODE);

        AdListener adInterstitialListener = new AdListener() {

            @Override
            public void onAdLoaded() {

                button.setEnabled(true);
            }

            @Override
            public void onAdFailedToLoad(int error) {

                Log.e("AdFailedInter", "Ad failed to load, errorcode: " + error);
                button.setEnabled(false);

                AdRequest adRequest = new AdRequest.Builder().build();
                mInterstitialAd.loadAd(adRequest);
            }

            @Override
            public void onAdClosed() {
                AdRequest adRequest = new AdRequest.Builder().build();
                button.setEnabled(false);
                mInterstitialAd.loadAd(adRequest);
            }
        };

        mInterstitialAd = new InterstitialAd(this);

        mInterstitialAd.setAdUnitId("ca-app-pub-3889032681139142/3864341552");
        mInterstitialAd.setAdListener(adInterstitialListener);
        Bundle extras = new Bundle();
        AdRequest.Builder adRequestBuilder = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, extras);

        mInterstitialAd.loadAd(adRequestBuilder.build());
        //CONFIGURA TEST DEVICE.
        if (BuildConfig.DEBUG) {
            adRequestBuilder.addTestDevice("E471AADF1D21337710F1244766497DF9");
        }

        initBanner(false);
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

    protected void initBanner(boolean isChangingConfiguration) {
        if (bottomBanner == null) {
            bottomBanner = new LinearLayout(this);
        } else {
            bottomBanner.removeAllViews();
            mAdViews.clear();
        }

        LinearLayout masterLayout = getAdRootView();
        if (masterLayout != null) {

            bottomBanner.setOrientation(LinearLayout.HORIZONTAL);
            bottomBanner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            if (bottomBanner.getParent() == null) {
                masterLayout.addView(bottomBanner);
            }


            if (bottomBanner.getWidth() <= 0 || isChangingConfiguration) {
                bottomBanner.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        initBottomBanner();
                        bottomBanner.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            } else {
                initBottomBanner();
            }
        }
    }

    private void initBottomBanner() {
        final int buttonWidth = 80;

        if (bottomBanner.getWidth() > 0) {
            initAdView((short) 0, new OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if(mAdViews.get(0) != null && mAdViews.get(0).getWidth() > 0) {
                        final double floatItems = (bottomBanner.getWidth() - buttonWidth) * 1.0 / mAdViews.get(0).getWidth();
                        final short totalAdViews = (short) Math.min(adsBannerUnitId.length, Math.floor(floatItems));

                        if (totalAdViews > 1) {
                            for (short i = 1; i < totalAdViews; i++) {
                                initAdView(i, null);
                            }
                        }

                        button = new ImageButton(AdsActivity.this);

                        button.setEnabled(false);
                        button.setBackground(ContextCompat.getDrawable(AdsActivity.this, R.drawable.block_color_dark));
                        button.setImageDrawable(ContextCompat.getDrawable(AdsActivity.this, R.drawable.ic_browser_open_white));
                        button.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
                        button.setOnClickListener(v -> CommonUtils.showDialog(AdsActivity.this, getString(R.string.help_message_ad, BuildConfig.SHORT_NAME), (dialog, which) -> showInterstitialAd(null), true));

                        bottomBanner.addView(button);

                        //REMOVE ALL LISTENERS, IF EXIST
                        for (short i = 1; i < totalAdViews; i++) {
                            mAdViews.get(i).getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    }
                }
            });

            adsBannerTrial = new int[mAdViews.size()];
        }
    }

    private void initAdView(short i, OnGlobalLayoutListener onGlobalLayoutListener) {
        final AdRequest.Builder adRequestBuilder = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, new Bundle());
        AdView adView = new AdView(AdsActivity.this);

        final short counter = i;
        AdListener adBannerListener = new AdListener() {

            @Override
            public void onAdLoaded() {
                adView.setVisibility(View.VISIBLE);

                adsBannerTrial[counter] = 0;
            }

            @Override
            public void onAdFailedToLoad(int error) {
                adView.setVisibility(View.GONE);

                Log.e("AdFailedBann", "Ad failed to load, errorcode: " + error + ". Trial:" + adsBannerTrial + ".");

                if (adsBannerTrial[counter] < 5) {
                    AdRequest adRequest = new AdRequest.Builder().build();
                    adView.loadAd(adRequest);
                    adsBannerTrial[counter]++;
                } else {
                    Log.e("AdFailedBann", "Could not receive ads. Dropping request...");
                }
            }

            @Override
            public void onAdClosed() {
            }
        };

        adView.setAdListener(adBannerListener);
        adView.setAdUnitId(adsBannerUnitId[i]);
        adView.setAdSize(AdSize.BANNER);


        adView.loadAd(adRequestBuilder.build());

        adView.setVisibility(View.GONE);
        adView.setLayoutParams(new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        bottomBanner.addView(adView);

        mAdViews.add(adView);

        if(onGlobalLayoutListener != null) {
            adView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
        }
    }
}