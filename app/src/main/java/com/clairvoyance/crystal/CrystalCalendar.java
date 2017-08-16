package com.clairvoyance.crystal;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
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
 * Sweet!
 */

class CrystalCalendar implements Serializable{

    private String userid;
    private ArrayList<ArrayList<CrystalEvent>> crystalEvents = new ArrayList<>();
    int eventCount = 0;

    CrystalCalendar(String id)
    {
        userid = id;
    }

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

    static CrystalCalendar read(Context context, CrystalCalendar calendar)
    {

        try {
            FileInputStream fIn = context.openFileInput("localCalendar");
            byte[] byteHolder = new byte[fIn.available()];
            fIn.read(byteHolder);
            calendar = (CrystalCalendar) Serializer.deserialize(byteHolder);
        } catch (IOException|ClassNotFoundException i)
        {
            Log.d("Read Error: ", i.getMessage());
        }
        return calendar;
    }

    void add(CrystalEvent event)
    {
        // Todo: Add based on start Date - for check

        // If empty, just add the event in a new date group
        if (crystalEvents.isEmpty())
        {
            ArrayList<CrystalEvent> newDateList = new ArrayList<>();
            newDateList.add(event);
            crystalEvents.add(newDateList);
            eventCount++;
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
                    eventCount++;
                    return;
                }
            }
            realDateList.add(event);
            eventCount++;
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
                    eventCount++;
                    return;
                }
            }
            crystalEvents.add(newDateList);
            eventCount++;
        }
    }

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

    ArrayList<ArrayList<CrystalEvent>> getEvents()
    {
        return crystalEvents;
    }

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
            eventButton.setText(event.displayTimeStringInAgenda(CrystalEvent.START_TIME) + " - " + event.displayTimeStringInAgenda(CrystalEvent.END_TIME) + ": " + event.get(CrystalEvent.NAME));
            innerLinearLayout.addView(eventButton);
        }

        return innerLinearLayout;

    }
}
