package com.clairvoyance.crystal;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.InputType;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
            Button eventButton = generateEventButton(context, event);
            eventButton.setText(event.displayTimeString(CrystalEvent.START_TIME, CrystalEvent.AGENDA_VIEW) + " - " + event.displayTimeString(CrystalEvent.END_TIME, CrystalEvent.AGENDA_VIEW) + ": " + event.get(CrystalEvent.NAME));
            innerLinearLayout.addView(eventButton);
        }

        return innerLinearLayout;

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

    static String getDateText(Calendar calendar){
        return DateFormat.getDateInstance().format(calendar.getTime());
    }

    static String getTimeText(Calendar calendar){
        return DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
    }

    static void generatePushNotifLayout(Context context, final LinearLayout mainLayout){
        LinearLayout pushNotifs = new LinearLayout(context);
        pushNotifs.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pushNotifs.setLayoutParams(layoutParams);

        final LinearLayout newNotifRow = new LinearLayout(context);
        newNotifRow.setOrientation(LinearLayout.HORIZONTAL);
        newNotifRow.setLayoutParams(layoutParams);

        EditText notifNumber = new EditText(context);
        Spinner notifTypes = new Spinner(context);
        Button deleteOption = new Button(context);

        newNotifRow.addView(notifNumber);
        newNotifRow.addView(notifTypes);
        newNotifRow.addView(deleteOption);
        pushNotifs.addView(newNotifRow);

        // NotifNumber Formatting
        int leftValueInPx = (int) (context.getResources().getDimension(R.dimen.activity_vertical_margin) / 2.);
        LinearLayout.LayoutParams notifNumberLayout = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2);
        notifNumberLayout.setMargins(leftValueInPx, 0, 0, 0);
        notifNumber.setLayoutParams(notifNumberLayout);

        notifNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        notifNumber.setPadding(notifNumber.getPaddingLeft(), 0, notifNumber.getPaddingRight(), 0);
        notifNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        notifNumber.setGravity(Gravity.CENTER);
        notifNumber.setBackgroundResource(R.drawable.border_black);

        // NotifType Formatting + Data
        LinearLayout.LayoutParams notifTypeLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        notifTypes.setLayoutParams(notifTypeLayout);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.timeMeasureOptions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        notifTypes.setAdapter(adapter);

        // Delete Button Formatting + Listener
        deleteOption.setText(R.string.del);
        deleteOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainLayout.removeView(newNotifRow);
            }
        });

        mainLayout.addView(pushNotifs, mainLayout.getChildCount() - 1);
    }

    /**
     * Generates a button that will lead into an Activity holding the event info (activity_view_event.xml)
     *
     * @param context the context the button will be generated in
     * @return a <code>Button</code> with all layout parameters for the Agenda view.
     */
    private static Button generateEventButton(final Context context, CrystalEvent event){
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

        return eventButton;
    }

}
