package net.ddns.andrewnetwork.ludothornsoundbox.ui.base.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings.Secure;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;

public class Utils {

    public static final String randomString(int lenght) {
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < lenght; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

  public static  String setEuro = " " + "€";

    public static Long getTimeStamp=System.currentTimeMillis() / 1000L;

    public static final String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }



    public static final String getAndroidId(Context context) {

        String androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
        if (androidId.equals("9774d56d682e549c") || androidId.equals("")) { // BACO
            // SU molti dispo android 2.2 hanno stesso udid. Per questi se non esiste gi� ne genero uno random e lo salvo, per� lo perdo se disinstallo l'app

            SharedPreferences prefs = context.getSharedPreferences("", Context.MODE_PRIVATE);
            if (prefs.contains("")) {
                androidId = prefs.getString("", "ERRORE_IN_GET_ANDROID_ID_BY_PREFS");
            } else {
                androidId = UUID.randomUUID().toString();
                prefs.edit().putString("", "AIDD -" + androidId).commit();
            }

        }
        return androidId;
    }


}
