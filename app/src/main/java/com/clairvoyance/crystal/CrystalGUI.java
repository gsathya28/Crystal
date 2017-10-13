package com.clairvoyance.crystal;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
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

    final static int LAYOUT = 0;
    final static int FONT = 1;

    final static int CRYSTAL_MAINAGENDA = 0;
    final static int CRYSTAL_NEW = 1;
    final static int CRYSTAL_VIEW = 2;
    final static int CRYSTAL_EDIT = 3;

    public CrystalGUI() {

    }

    static void generateGUI(Context context, int activity){
        switch (activity){
            case CRYSTAL_NEW:
                break;
            case CRYSTAL_EDIT:
                break;
            case CRYSTAL_VIEW:
                break;
            default:
                Log.d("GUI Generate Error:", "ID not found!");
        }
    }

    static int getFontSizeInPx(Context context, int value, int type) {
        Resources r = context.getResources();
        switch (type){
            case FONT:
                return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, r.getDisplayMetrics());
            case LAYOUT:
                return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, r.getDisplayMetrics());
        }

        return 0;
    }

    /**
     * Generates a layout of all the events on a particular date for the Agenda View (activity_main.xml)
     *
     * @param context the context in which the layout is being generated in
     * @param dateOfEvent the date of the <code>ArrayList</code> of events.
     * @return a <code>LinearLayout</code> with all the events on a certain date.
     */
    static LinearLayout generateAgendaLinearLayout(Context context, ArrayList<CrystalEvent> dateOfEvent){

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

        int fontSizeInPx = getFontSizeInPx(context, 12, FONT);
        dateTextView.setTextSize(fontSizeInPx);
        dateTextView.setTypeface(null, Typeface.BOLD);

        innerLinearLayout.addView(dateTextView);

        for (CrystalEvent event : dateOfEvent)
        {
            Button eventButton = generateEventButton(event, context);
            innerLinearLayout.addView(eventButton);
        }

        return innerLinearLayout;
    }

    static Button generateEventButton(final CrystalEvent event, final Context context){
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

        final CrystalEvent buttonEvent = event;

        eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewEventIntent = new Intent(context, ViewEvent.class);
                viewEventIntent.putExtra("Event", buttonEvent);
                context.startActivity(viewEventIntent);
            }
        });

        eventButton.setText(event.displayTimeString(CrystalEvent.START_TIME, CrystalEvent.AGENDA_VIEW) + " - " + event.displayTimeString(CrystalEvent.END_TIME, CrystalEvent.AGENDA_VIEW) + ": " + event.get(CrystalEvent.NAME));
        return eventButton;
    }


}