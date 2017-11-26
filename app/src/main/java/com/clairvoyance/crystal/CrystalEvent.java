package com.clairvoyance.crystal;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Sathya Govindarajan on 8/18/17
 *
 * The <code>CrystalEvent</code> class is a class that provides methods
 * for handling an event within a calendar.
 */

class CrystalEvent implements Serializable, CrystalInstant {

    /**
     * All the values that this class holds
     */
    private Calendar startTime;
    private Calendar endTime;
    private boolean isAllDay = false;
    private String name = "Untitled Event";
    private String notes = "";
    private String id;
    private ArrayList<CrystalAlarm> alarms;

    /**
     *
     * Constructs an event with the specified start and end time and the calendar it is saved in.
     *
     * @param inStartTime the start time
     * @param inEndTime the end time
     * @param localCalendar the calendar the event is saved in
     */
    CrystalEvent(Calendar inStartTime, Calendar inEndTime, CrystalCalendar localCalendar)
    {
        startTime = inStartTime;
        endTime = inEndTime;
        id = Build.HOST + (localCalendar.eventCount + 1);
    }

    /**
     *
     * @return the <code>Calendar</code> representing the start time
     */
    public Calendar getStartTime()
    {
        return startTime;
    }

    /**
     *
     * @return the <code>Calendar</code> representing the end time
     */
    public Calendar getEndTime() { return endTime; }

    /**
     * Displays times in a String Format using <code>DateFormat</code>
     *
     * @param field the time to be displayed
     * @param activity the activity in which it should be displayed in.
     * @return the time in a String.
     */
    public String displayTimeString(int field, int activity)
    {
        String finalString;
        switch (activity)
        {
            case VIEW_EVENT:
                switch (field) {
                    case START_TIME:
                        String startDateText = DateFormat.getDateInstance().format(startTime.getTime());
                        String startTimeText = DateFormat.getTimeInstance(DateFormat.SHORT).format(startTime.getTime());

                        finalString = startTimeText + "\n" + startDateText;
                        return finalString;
                    case END_TIME:
                        String endDateText = DateFormat.getDateInstance().format(endTime.getTime());
                        String endTimeText = DateFormat.getTimeInstance(DateFormat.SHORT).format(endTime.getTime());

                        finalString = endTimeText + "\n" + endDateText;
                        return finalString;
                }
            case AGENDA_VIEW:
                switch (field) {
                    case START_TIME:
                        return DateFormat.getTimeInstance(DateFormat.SHORT).format(startTime.getTime());
                    case END_TIME:
                        return DateFormat.getTimeInstance(DateFormat.SHORT).format(endTime.getTime());
                }
        }

        return "";
    }

    /**
     * Sets the boolean values of the CrystalEvent
     *
     * @param field the variable to be changed
     * @param value the value it needs to be changed to
     */
    public void set(int field, boolean value)
    {
        switch (field)
        {
            case ALL_DAY:
                isAllDay = value;
                break;
        }
    }

    /**
     * Sets the <code>String</code> values of the CrystalEvent
     *
     * @param field the variable to be changed
     * @param value the value it needs to be changed to
     * @return a boolean regarding the success of the change
     */
    public boolean set(int field, String value)
    {
        if(!value.equals("")) {
            switch (field) {
                case NAME:
                    name = value;
                    return true;
                case NOTES:
                    notes = value;
                    return true;
            }
        }
        return false;
    }

    /**
     * Returns the String values of the CrystalEvent
     *
     * @param field - the String variable requested
     * @return - the String value
     */
    public String get(int field)
    {
        switch (field)
        {
            case NAME:
                return name;
            case ID:
                return id;
            case NOTES:
                return notes;
        }
        return "";
    }

    /**
     *
     * @return whether the event is an all-day event or not.
     */
    boolean isAllDay()
    {
        return isAllDay;
    }

    public void setStartNotificationIntent(Context context) {
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, NotificationPublisher.START);
        notificationIntent.putExtra(NotificationPublisher.NAME, name);
        notificationIntent.putExtra(NotificationPublisher.NOTES, notes);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationPublisher.scheduleNotification(context, startTime, pendingIntent);
    }

    public void setAlarms(Context context, ArrayList<CrystalAlarm> crystalAlarms){
        alarms = crystalAlarms;
        for (CrystalAlarm alarm: alarms) {
            alarm.setNotificationIntent(context);
        }
    }

}
