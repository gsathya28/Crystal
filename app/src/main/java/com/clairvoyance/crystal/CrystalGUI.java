package com.clairvoyance.crystal;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Sathya on 8/12/2017.
 * GUI Docs
 */

class CrystalGUI {

    private final static int LAYOUT = 0;
    final static int FONT = 1;

    public CrystalGUI() {

    }

    static LinearLayout generateOuterAgendaLinearLayout(Context context, LinearLayout outerLayout, ArrayList<ArrayList<CrystalEvent>> eventList){

        for (ArrayList<CrystalEvent> dateOfEvent : eventList) {
            LinearLayout innerLinearLayout = generateInnerAgendaLinearLayout(context, dateOfEvent);
            outerLayout.addView(innerLinearLayout);
        }
        return outerLayout;
    }

    /**
     * Generates a layout of all the events on a particular date for the Agenda View (activity_main.xml/MainActivity.this)
     *
     * @param context the context in which the layout is being generated in
     * @param dateOfEvent the date of the <code>ArrayList</code> of events.
     * @return a <code>LinearLayout</code> with all the events on a certain date.
     */
    private static LinearLayout generateInnerAgendaLinearLayout(Context context, ArrayList<CrystalEvent> dateOfEvent){

        LinearLayout innerLinearLayout = new LinearLayout(context);
        innerLinearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        innerLinearLayout.setLayoutParams(layoutParams);

        TextView dateTextView = new TextView(context);
        Calendar startCalendar = dateOfEvent.get(0).getStartTime();
        String dateText = DateFormat.getDateInstance(DateFormat.SHORT).format(startCalendar.getTime());
        dateTextView.setText(dateText);

        int topValueInPx = (int) context.getResources().getDimension(R.dimen.activity_vertical_margin);
        int bottomValueInPx = (int) context.getResources().getDimension(R.dimen.activity_vertical_margin);
        bottomValueInPx = bottomValueInPx / 2;
        int leftValueInPx = (int) context.getResources().getDimension(R.dimen.activity_horizontal_margin);

        layoutParams.setMargins(leftValueInPx, topValueInPx, leftValueInPx, bottomValueInPx);
        dateTextView.setLayoutParams(layoutParams);

        Resources r = context.getResources();
        int fontSizeInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, r.getDisplayMetrics());
        dateTextView.setTextSize(fontSizeInPx);
        dateTextView.setTypeface(null, Typeface.BOLD);

        innerLinearLayout.addView(dateTextView);

        for (CrystalEvent event : dateOfEvent)
        {

            Button eventButton = event.generateButton(context);
            eventButton.setText(event.displayTimeString(CrystalEvent.START_TIME, CrystalEvent.AGENDA_VIEW) + " - " + event.displayTimeString(CrystalEvent.END_TIME, CrystalEvent.AGENDA_VIEW) + ": " + event.get(CrystalEvent.NAME));
            innerLinearLayout.addView(eventButton);
        }

        return innerLinearLayout;

    }

    static int getFontSizeInPx(Context context, int value, int type)
    {
        Resources r = context.getResources();
        switch (type){
            case FONT:
                return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, r.getDisplayMetrics());
            case LAYOUT:
                return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, r.getDisplayMetrics());
        }

        return 0;
    }

}
