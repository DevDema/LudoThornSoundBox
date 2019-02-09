package net.ddns.andrewnetwork.ludothornsoundbox.ui.base.core;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.CalendarContract;

import net.ddns.andrewnetwork.ludothornsoundbox.R;

import java.io.File;
import java.util.Calendar;

import androidx.fragment.app.Fragment;
/**
 * @author Antonio Cornacchia
 * @version 1.2
 * @date: 07/10/2016
 */
public class IntentManager {

    private static IntentTransaction mTransaction = new IntentTransaction();

    private static Context mContext;

    public static IntentTransaction launch(Context context) {
        mContext = context;
        return mTransaction;
    }

    public static IntentTransaction launch(Fragment fragment) {
        return launch(fragment.getActivity());
    }

    public static class IntentTransaction {

        public void mapIntent(String city, double latitude, double longitude) {
            String label = city;
            String uriBegin = "geo:" + latitude + "," + longitude;
            String query = latitude + "," + longitude + "(" + label + ")";
            String encodedQuery = Uri.encode(query);
            String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
            Uri uri = Uri.parse(uriString);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            mContext.startActivity(intent);
        }

        public void callIntent(String telephone) {
            String uri = "tel:" + telephone.trim();
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(uri));
            mContext.startActivity(intent);
        }

        public void browserIntent(String website) {
            Uri uri = Uri.parse(website);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            mContext.startActivity(intent);
        }

        public void mailIntent(String email) {
            Uri uri = Uri.parse("mailto:" + email);
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
            mContext.startActivity(Intent.createChooser(emailIntent,
                    mContext.getString(R.string.send_email)));
        }

        public void eventIntent(String title, String description, Calendar start, Calendar end) {
            Intent intent = new Intent(Intent.ACTION_EDIT);
            intent.setType("vnd.android.cursor.item/event");
            intent.putExtra(CalendarContract.Events.TITLE, title);
            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                    start.getTimeInMillis());
            intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                    end.getTimeInMillis());
            intent.putExtra(CalendarContract.Events.ALL_DAY, false); // periodicity
            intent.putExtra(CalendarContract.Events.DESCRIPTION, description);
        }

        public void takePicture(Fragment fragment, int RESULT) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            File f = new File(Environment.getExternalStorageDirectory(), "POST_IMAGE.jpg");
            try {
                fragment.startActivityForResult(
                        Intent.createChooser(intent, "Select a File to Upload"),
                        RESULT);
            } catch (ActivityNotFoundException ex) {
                throw new ActivityNotFoundException();
            }
        }

        public void share(String message){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,  message);
            sendIntent.setType("text/plain");
            mContext.startActivity(sendIntent);
        }
    }

}
