package net.ddns.andrewnetwork.ludothornsoundbox.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.MainActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.videoinfo.VideoInformationActivity;

import java.util.Map;

public class FireBaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static final int ID_NOTIFICATION_CHANNEL_VIDEOS = Integer.MAX_VALUE;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Log.d(TAG, "Message Notification Click Action: " + remoteMessage.getNotification().getClickAction());
        }

        sendNotification(remoteMessage);
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        //TODO sendRegistrationToServer(token);
    }

    private void sendNotification(RemoteMessage message) {


        RemoteMessage.Notification notification = message.getNotification();
        Map<String, String> data = message.getData();
        if (notification != null) {
            Intent intent = new Intent(this, VideoInformationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP);

            for (Map.Entry<String, String> entry : data.entrySet()) {
                intent.putExtra(entry.getKey(), entry.getValue());
            }

            if (notification.getClickAction() != null) {
                intent.setAction(notification.getClickAction());
            }



            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            String channelId = getString(R.string.notifiche_canale_channel_id);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.mipmap.ic_action_launcher_1_layer)
                            .setContentTitle(notification.getTitle())
                            .setContentText(notification.getBody())
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_action_launcher_1_layer))
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(ID_NOTIFICATION_CHANNEL_VIDEOS, notificationBuilder.build());
        }
    }
}
