package com.clairvoyance.crystal;

import android.content.Context;
import android.util.Log;
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

/**
 * Created by Sathya on 8/9/2017.
 * Sweet!
 */

class CrystalCalendar implements Serializable{

    private String userid;
    private ArrayList<ArrayList<CrystalEvent>> crystalEvents = new ArrayList<>();

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

        if (crystalEvents.isEmpty())
        {
            ArrayList<CrystalEvent> newDateList = new ArrayList<>();
            newDateList.add(event);
            crystalEvents.add(newDateList);
            return;
        }

        boolean foundDate = false;
        Calendar startCalendar = event.getStartTime();

        for (ArrayList<CrystalEvent> dateList : crystalEvents)
        {
            Calendar date = dateList.get(0).getStartTime();
            if (date.get(Calendar.DATE) == startCalendar.get(Calendar.DATE))
            {
                for (CrystalEvent dateEvent : dateList)
                {
                    if (dateEvent.getStartTime().getTimeInMillis() >= startCalendar.getTimeInMillis())
                    {
                        dateList.add(dateList.indexOf(dateEvent), event);
                        return;
                    }
                }
                dateList.add(event);
                foundDate = true;
            }
        }

        if (!foundDate) {
            ArrayList<CrystalEvent> newDateList = new ArrayList<>();
            newDateList.add(event);
            for (ArrayList<CrystalEvent> dateList : crystalEvents) {
                if (dateList.get(0).getStartTime().get(Calendar.DATE) > startCalendar.get(Calendar.DATE)) {
                    crystalEvents.add(crystalEvents.indexOf(dateList), newDateList);
                    return;
                }
            }
            crystalEvents.add(newDateList);
        }

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
        dateTextView.setLayoutParams(layoutParams);

        innerLinearLayout.addView(dateTextView);

        for (CrystalEvent event : dateOfEvent)
        {
            Button eventButton = event.generateButton(context);
            eventButton.setText("1");
            innerLinearLayout.addView(eventButton);
        }

        return innerLinearLayout;

    }

}
