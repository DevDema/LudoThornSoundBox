package net.ddns.andrewnetwork.ludothornsoundbox.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public abstract class StringUtils {

    public static boolean nonEmptyNonNull(String string1) {
        return string1 != null && !string1.isEmpty();
    }

    public static boolean nonEmptyNonNull(List list) {
        return list != null && !list.isEmpty();
    }

    public static boolean nonEmptyNonNull(CharSequence string1) {
        return string1 != null && string1.length() > 0;
    }

    public static boolean nonEmptyNonNull(EditText editText) {
        return editText.getText() != null && !editText.getText().toString().isEmpty();
    }

    public static boolean nonEmptyNonNull(TextView textView) {
        return textView.getText() != null && !textView.getText().toString().isEmpty();
    }

    public static String valueOf(BigInteger bigInteger) {
        if (bigInteger == null) {
            return null;
        }

        return String.valueOf(bigInteger.intValue());
    }

    public static int getActionIdByString(String fragment) {
        if (fragment != null) {
            switch (fragment) {
                default:
                case "Home":
                    return R.id.action_home;
                case "Preferiti":
                    return R.id.action_favorites;
                case "Casuale":
                    return R.id.action_random;
                case "Video":
                    return R.id.action_video;
            }
        }

        return R.id.action_home;
    }

    public static String abbreviate(String longString) {
        return longString.substring(0, 3) + ".";
    }

    public static String buildPossibleFileName(LudoAudio audio) {
        return "n" + audio.getOrder() + "_" + audio.getTitle().replace(" ", "_");
    }

    public static List<String> buildRandomStringList(Context context) {
        final Resources resources = context.getResources();

        String readFile = FileUtils.readTextFile(resources.openRawResource(R.raw.frasi_random));

        return Arrays.asList(readFile.replaceAll("- ", "").split("[\\r?\\n]+"));
    }

    public static boolean startsWith(String url, final String[] EXCLUDED_URLS) {
        for(String excluded : EXCLUDED_URLS) {
            if(url.startsWith(excluded)) {
                return true;
            }
        }

        return false;
    }

    /**
     *
     * @param url string to be compared
     * @param EXCLUDED_URLS array of strings that the string should end with
     * @return the suffix the strings ends by.
     */
    public static String endsWith(String url, final String[] EXCLUDED_URLS) {
        for(String excluded : EXCLUDED_URLS) {
            if(url.endsWith(excluded)) {
                return excluded;
            }
        }

        return null;
    }

    public static String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String h = Integer.toHexString(0xFF & b);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Log.e("MD5Exception",e.getMessage());
        }
        return "";
    }
}
