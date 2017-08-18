package com.clairvoyance.crystal;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

/**
 * Created by Sathya on 8/9/2017.
 *
 * The <code>CrystalCalendar</code> class is a class that acts a calendar, holding all events, and provides methods for data transfer,
 * iCal conversion, and manipulation of events.
 *
 */

class CrystalCalendar implements Serializable{

    /**
     * All the values this class holds
     */
    private String userid;
    private ArrayList<ArrayList<CrystalEvent>> crystalEvents = new ArrayList<>();
    int eventCount = 0;

    /**
     * A constructor that only requires an id.
     *
     * @param id - an id to distinguish this localCalendar from other calendars
     */
    private CrystalCalendar(String id)
    {
        userid = id;
    }

    /**
     * Saves the calendar data in the internal storage - filename: localCalendar.txt (will change)
     *
     * @param context the <code>Context</code> in which the save is being done in, so internal storage writing permissions are given
     * @return boolean regarding the success of the save
     */
    boolean save(Context context)
    {
        try {
            FileOutputStream fOut = context.openFileOutput("localCalendar", Context.MODE_PRIVATE);
            fOut.write(Serializer.serialize(this));
            fOut.close();
        } catch (IOException i) {
            Log.d("Save Error: ", i.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Reads the calendar data from the internal storage - filename: localCalendar.txt
     *
     * @param context the <code>Context</code> in which the read is being done in, so internal storage writing permissions are given
     * @return <code>CrystalCalendar</code> object with data read from localCalendar.txt
     */
    static CrystalCalendar read(Context context)
    {
        CrystalCalendar calendar = new CrystalCalendar(Build.ID + "@clairvoyance.com");
        try {
            FileInputStream fIn = context.openFileInput("localCalendar");
            byte[] byteHolder = new byte[fIn.available()];
            int x = fIn.read(byteHolder);
            if (x != -1)
                calendar = (CrystalCalendar) Serializer.deserialize(byteHolder);
        } catch (IOException|ClassNotFoundException i)
        {
            Log.d("Read Error: ", i.getMessage());
        }
        return calendar;
    }

    /**
     * Adds an event to the list of events in the calendar sorted by date of the start time
     *
     * @param event the event to be added
     */
    void add(CrystalEvent event)
    {
        // Todo: Add based on start Date - for check
        eventCount++;
        // If empty, just add the event in a new date group
        if (crystalEvents.isEmpty())
        {
            ArrayList<CrystalEvent> newDateList = new ArrayList<>();
            newDateList.add(event);
            crystalEvents.add(newDateList);
            return;
        }

        // Strip the calendars to only take the date into account
        Calendar startCalendar = event.getStartTime();
        Calendar strippedStartCalendar = (Calendar) startCalendar.clone();
        strippedStartCalendar.set(startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DATE), 0, 0, 0);
        strippedStartCalendar.set(Calendar.MILLISECOND, 0);

        // Find the date group we need.
        ArrayList<CrystalEvent> realDateList = findDate(event);
        // If the date group is found:
        if (realDateList != null)
        {
            for (CrystalEvent dateEvent : realDateList)
            {
                if (dateEvent.getStartTime().getTimeInMillis() >= startCalendar.getTimeInMillis())
                {
                    realDateList.add(realDateList.indexOf(dateEvent), event);
                    return;
                }
            }
            realDateList.add(event);
        }
        else { // If it isn't found
            ArrayList<CrystalEvent> newDateList = new ArrayList<>();
            newDateList.add(event);

            for (ArrayList<CrystalEvent> dateList : crystalEvents) {
                Calendar date = dateList.get(0).getStartTime();
                Calendar strippedDate = (Calendar) date.clone();
                strippedDate.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE), 0, 0, 0);
                strippedDate.set(Calendar.MILLISECOND, 0);

                if (strippedStartCalendar.before(strippedDate)) {

                    crystalEvents.add(crystalEvents.indexOf(dateList), newDateList);
                    return;
                }
            }
            crystalEvents.add(newDateList);
        }
    }

    /**
     * Removes an event from the list of events
     *
     * @param event the event to be deleted
     * @return boolean regarding the success of the removal
     */
    boolean remove(CrystalEvent event)
    {
        ArrayList<CrystalEvent> dateList = findDate(event);
        if (dateList != null) {

            int index = crystalEvents.indexOf(dateList);
            crystalEvents.remove(dateList);
            Iterator<CrystalEvent> i = dateList.iterator();

            while (i.hasNext())
            {
                CrystalEvent eventCheck = i.next();
                if(eventCheck.get(CrystalEvent.ID).equals(event.get(CrystalEvent.ID)))
                {
                    i.remove();
                }
            }

            if (dateList.isEmpty())
            {
                return true;
            }

            if (index >= crystalEvents.size())
            {
                crystalEvents.add(dateList);
            }
            else
            {
                crystalEvents.add(index, dateList);
            }
            return true;
        }
        return false;
    }

    /**
     * Finds the <code>ArrayList</code> that holds all the events with the same date as the event passed in
     *
     * @param event - the event we need to find the date for
     * @return <code>ArrayList</code> if there are events with same date as the event passed in, <code>null</code> if no events with the same date are found
     */
    @Nullable
    private ArrayList<CrystalEvent> findDate(CrystalEvent event)
    {
        Calendar startCalendar = event.getStartTime();
        Calendar strippedStartCalendar = (Calendar) startCalendar.clone();
        strippedStartCalendar.set(startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DATE), 0, 0, 0);
        strippedStartCalendar.set(Calendar.MILLISECOND, 0);

        for (ArrayList<CrystalEvent> dateList : crystalEvents)
        {
            Calendar date = dateList.get(0).getStartTime();
            Calendar strippedDate = (Calendar) date.clone();
            strippedDate.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE), 0, 0, 0);
            strippedDate.set(Calendar.MILLISECOND, 0);

            if (strippedStartCalendar.equals(strippedDate))
            {
                return dateList;
            }
        }

        return null;
    }

    /**
     *
     * @return the <code>ArrayList</code> of all the events sorted by date (in <code>ArrayLists</code>)
     */
    ArrayList<ArrayList<CrystalEvent>> getEvents()
    {
        return crystalEvents;
    }

    /**
     * Generates a layout of all the events on a particular date for the Agenda View (activity_main.xml)
     *
     * @param context the context in which the layout is being generated in
     * @param dateOfEvent the date of the <code>ArrayList</code> of events.
     * @return a <code>LinearLayout</code> with all the events on a certain date.
     */
    LinearLayout generateAgendaLinearLayout(Context context, ArrayList<CrystalEvent> dateOfEvent){

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
}
