package com.clairvoyance.crystal;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;

/**
 * Created by Sathya on 8/9/2017.
 * Wow - no way!
 */

class CrystalEvent implements Serializable {

    private Calendar startTime;
    private Calendar endTime;
    private boolean isAllday = false;
    private boolean hasReminders;
    private String name = "Untitled Event";
    private String notes = "";
    private String id;

    final static int ALL_DAY = 0;
    final static int HAS_REMINDERS = 1;
    final static int NAME = 2;
    final static int NOTES = 3;
    final static int START_TIME = 4;
    final static int END_TIME = 5;
    final static int ID = 6;

    CrystalEvent(Calendar inStartTime, Calendar inEndTime, CrystalCalendar localCalendar)
    {
        startTime = inStartTime;
        endTime = inEndTime;
        id = Build.HOST + (localCalendar.eventCount + 1);
    }

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

    protected void generatePushNotif()
    {

    }

    protected Calendar getStartTime()
    {
        return startTime;
    }

    protected Calendar getEndTime() { return endTime; }

    protected String displayTimeStringInView(int field)
    {
        String finalString;
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
        return "";
    }

    protected String displayTimeStringInAgenda(int field)
    {
        switch (field) {
            case START_TIME:
                return DateFormat.getTimeInstance(DateFormat.SHORT).format(startTime.getTime());
            case END_TIME:
                return DateFormat.getTimeInstance(DateFormat.SHORT).format(endTime.getTime());
        }
        return "";
    }

    boolean set(int field, boolean value)
    {
        switch (field)
        {
            case ALL_DAY:
                isAllday = value;
                return true;
        }

        return false;
    }

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

    String get(int field)
    {
        switch (field)
        {
            case NAME:
                return name;
            case ID:
                return id;
        }
        return "";
    }

}
