package com.clairvoyance.crystal;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

/**
 * Created by Sathya on 8/22/2017.
 * Hello!
 */

public class NotificationPublisher extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id";
    public static String NAME = "name";
    public static String NOTES = "notes";

    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = getNotification(context, intent);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        notificationManager.notify(id, notification);
    }

    Notification getNotification(Context context, Intent intent) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(intent.getStringExtra(NAME));
        builder.setContentText(intent.getStringExtra(NOTES));
        builder.setSmallIcon(R.drawable.ic_notifications_black_24dp);
        builder.setVibrate(new long[] {1000, });
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(sound);

        Intent resultIntent = new Intent(context, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);

        return builder.build();
    }
}