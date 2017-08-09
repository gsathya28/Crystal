package com.clairvoyance.crystal;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Sathya on 8/9/2017.
 * Wow - no way!
 */

public class CrystalEvent implements Serializable {



    private Calendar startTime;
    private Calendar endTime;
    private boolean isAlday;
    private boolean hasReminders;



    public final static int ALL_DAY = 0;
    public final static int HAS_REMINDERS = 1;
    public final static int NAME = 2;
    public final static int NOTES = 3;



    public CrystalEvent(Calendar inStartTime, Calendar inEndTime)
    {
        startTime = inStartTime;
        endTime = inEndTime;
    }

    protected Button generateButton(Context context)
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
        Resources r = context.getResources();

        int fontSizeInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, r.getDisplayMetrics());
        eventButton.setTextSize(fontSizeInPx);

        // Formatting Button - Text Alignment
        int topPaddingAdjustment = 5;
        int leftPaddingAdjustment = 16;
        eventButton.setGravity(3);
        eventButton.setPadding(
                eventButton.getPaddingLeft() + leftPaddingAdjustment,
                eventButton.getPaddingTop() + topPaddingAdjustment,
                eventButton.getPaddingRight() - leftPaddingAdjustment,
                eventButton.getPaddingBottom() - topPaddingAdjustment
        );

        return eventButton;
    }

    protected void generatePushNotif()
    {

    }

    protected Calendar[] getTimes()
    {
        Calendar[] times = {startTime, endTime};
        return times;
    }

    // Todo: Will return String[]
    protected void displayTimeString()
    {

    }

    protected void set(int field, boolean value)
    {

    }

    protected void set(int field, String value)
    {

    }
}
