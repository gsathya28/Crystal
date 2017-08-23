package com.clairvoyance.crystal;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;

/**
 * Created by Sathya Govindarajan on 8/18/17
 *
 * The <code>CrystalEvent</code> class is a class that provides methods
 * for handling an event within a calendar.
 */

class CrystalEvent implements Serializable {

    /**
     * All the values that this class holds
     */
    private Calendar startTime;
    private Calendar endTime;
    private boolean isAllDay = false;
    private boolean hasReminders = false;
    private String name = "Untitled Event";
    private String notes = "";
    private String id;

    /**
     * All the fields that this class holds
     */
    final static int ALL_DAY = 0;
    private final static int HAS_REMINDERS = 1;
    final static int NAME = 2;
    final static int NOTES = 3;
    final static int START_TIME = 4;
    final static int END_TIME = 5;
    final static int ID = 6;
    final static int AGENDA_VIEW = 7;
    final static int VIEW_EVENT = 8;

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
     * Generates a button that will lead into an Activity holding the event info (activity_view_event.xml)
     *
     * @param context the context the button will be generated in
     * @return a <code>Button</code> with all layout parameters for the Agenda view.
     */
    Button generateButton(final Context context)
    {
        Button eventButton = new Button(context);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        int topValueInPx = (int) context.getResources().getDimension(R.dimen.activity_vertical_margin);
        int bottomValueInPx = (int) context.getResources().getDimension(R.dimen.activity_vertical_margin);
        bottomValueInPx = bottomValueInPx / 2;
        int leftValueInPx = (int) context.getResources().getDimension(R.dimen.activity_horizontal_margin);
            /*
            if (i != 0)
            {
                topValueInPx = topValueInPx / 2;
            }
            */
        params.setMargins(leftValueInPx, topValueInPx, leftValueInPx, bottomValueInPx);
        eventButton.setLayoutParams(params);

        // Formatting the TextView - Background and Text Color
        eventButton.setTextColor(Color.parseColor("#FFFFFF"));
        eventButton.setBackgroundColor(Color.parseColor("#0000FF"));

        // Formatting Font -
        int fontSizeInPx = CrystalGUI.getFontSizeInPx(context, 10, CrystalGUI.FONT);
        eventButton.setTextSize(fontSizeInPx);
        eventButton.setSingleLine();
        eventButton.setEllipsize(TextUtils.TruncateAt.END);

        // Formatting Button - Text Alignment
        int topPaddingAdjustment = 5;
        int leftPaddingAdjustment = 16;
        eventButton.setGravity(3);
        eventButton.setPadding(
                eventButton.getPaddingLeft() + leftPaddingAdjustment,
                eventButton.getPaddingTop() + topPaddingAdjustment,
                eventButton.getPaddingRight() + leftPaddingAdjustment,
                eventButton.getPaddingBottom() - topPaddingAdjustment
        );

        final CrystalEvent buttonEvent = this;

        eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewEventIntent = new Intent(context, ViewEvent.class);
                viewEventIntent.putExtra("Event", buttonEvent);
                context.startActivity(viewEventIntent);
            }
        });

        return eventButton;
    }

    /**
     *
     * @return the <code>Calendar</code> representing the start time
     */
    Calendar getStartTime()
    {
        return startTime;
    }

    /**
     *
     * @return the <code>Calendar</code> representing the end time
     */
    Calendar getEndTime() { return endTime; }

    /**
     * Displays times in a String Format using <code>DateFormat</code>
     *
     * @param field the time to be displayed
     * @param activity the activity in which it should be displayed in.
     * @return the time in a String.
     */
    String displayTimeString(int field, int activity)
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
    void set(int field, boolean value)
    {
        switch (field)
        {
            case ALL_DAY:
                isAllDay = value;
                break;
            case HAS_REMINDERS:
                hasReminders = value;
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
    boolean set(int field, String value)
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
    String get(int field)
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

    /**
     *
     * @return whether the event has any reminders before the event starts.
     */
    boolean hasReminders()
    {
        return hasReminders;
    }

    void scheduleNotification(Context context) {
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NAME, name);
        notificationIntent.putExtra(NotificationPublisher.NOTES, notes);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = startTime.getTimeInMillis();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
        }
        else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
        }
    }




}
