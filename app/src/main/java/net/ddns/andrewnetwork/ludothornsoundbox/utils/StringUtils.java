package net.ddns.andrewnetwork.ludothornsoundbox.utils;

import android.widget.EditText;
import android.widget.TextView;

import java.math.BigInteger;
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
        if(bigInteger == null) {
            return null;
        }

        return String.valueOf(bigInteger.intValue());
    }
}
