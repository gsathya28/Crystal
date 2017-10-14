package com.clairvoyance.crystal;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Sathya on 8/26/2017.
 * Yup!
 */

class CrystalAlarm implements Serializable {

    private Calendar alarmTime;
    private String name = "Untitled Event";
    private String notes = "";

    /**
     * All the fields that this class holds
     */

    /*
    final static int NAME = 2;
    final static int NOTES = 3;
    final static int START_TIME = 4;
    final static int ID = 6;
    */

    CrystalAlarm(Calendar time) {
        alarmTime = time;
    }

    void setOffsetText(CrystalEvent event, int offset, String type) {
        String rightType = type;
        if (offset == 1)
            rightType = type.substring(0, type.length() - 1);

        name = "Event " + event.get(CrystalEvent.NAME) + " will start in " + offset + " " + rightType;
        notes = "Get to it!";
    }

    void setNotificationIntent(Context context) {
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NAME, name);
        notificationIntent.putExtra(NotificationPublisher.NOTES, notes);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NotificationPublisher.BEFORE_START, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationPublisher.scheduleNotification(context, alarmTime, pendingIntent);
    }

    static Calendar offsetCalendar(Calendar calendar, int offset, String type)
    {
        Calendar offsetCalendar = (Calendar) calendar.clone();
        switch (type){
            case "minutes":
                offsetCalendar.add(Calendar.MINUTE, offset);
                return offsetCalendar;
            case "hours":
                offsetCalendar.add(Calendar.HOUR_OF_DAY, offset);
                return offsetCalendar;
            case "days":
                offsetCalendar.add(Calendar.DAY_OF_YEAR, offset);
                return offsetCalendar;
            case "weeks":
                offsetCalendar.add(Calendar.WEEK_OF_YEAR, offset);
                return offsetCalendar;
            default:
                return offsetCalendar;
        }
    }
}
