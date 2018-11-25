package net.ddns.andrewnetwork.ludothornsoundbox.model;

import android.media.MediaPlayer;

import com.google.android.gms.ads.InterstitialAd;


public abstract class User {

    private static boolean premiumUser = false;

    private static int favlimit = 5;

    private static int adLimit = 5;

    private static float adCounter;


    private static void increaseAdCounter() {
        double increaser = 0.5 + Math.random() * (1 - 0.5);

         adCounter += increaser;
    }

    public static int getFavlimit() {
        return favlimit;
    }

    public static boolean isPremiumUser() {
        return premiumUser;
    }

    private static boolean areAdsReady(InterstitialAd interstitialAd) {
        increaseAdCounter();
        if(adCounter > adLimit && interstitialAd.isLoaded()) {
            adCounter = 1;
            return true;

        }
        return false;
    }

    public static boolean loadAds(InterstitialAd interstitialAd, MediaPlayer mediaPlayer) {
        if(areAdsReady(interstitialAd)) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            interstitialAd.show();
            return true;
        }
        return false;
    }
}
