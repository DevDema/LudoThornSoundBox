package net.ddns.andrewnetwork.ludothornsoundbox.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public abstract class PhoneUtils {

    public static boolean isPhoneConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            return false;
        }

        return ni.isConnected();
    }
}
