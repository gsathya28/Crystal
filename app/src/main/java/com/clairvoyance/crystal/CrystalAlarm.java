package com.clairvoyance.crystal;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Sathya on 8/26/2017.
 */

public class CrystalAlarm implements Serializable {

    private Calendar alarmTime;
    private String name = "Untitled Event";
    private String notes = "";
    private String id;
    private PendingIntent pendingIntent;

    /**
     * All the fields that this class holds
     */
    final static int NAME = 2;
    final static int NOTES = 3;
    final static int START_TIME = 4;
    final static int ID = 6;

    CrystalAlarm(Context context, Calendar time) {
        alarmTime = time;
    }

    void setOffsetText(CrystalEvent event, int offset, String type) {
        String rightType = type;
        if (offset == 1)
            rightType = type.substring(0, type.length() - 1);

        name = "Event " + event.get(CrystalEvent.NAME) + " will start in " + offset + " " + rightType;
    }

    void setNotificationIntent(Context context) {
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NAME, name);
        notificationIntent.putExtra(NotificationPublisher.NOTES, notes);
        pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationPublisher.scheduleNotification(context, alarmTime, pendingIntent);
    }
}
