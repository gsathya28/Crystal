package com.clairvoyance.crystal;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
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

// TODO: Optimize add, remove and findDate algorithms

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
            FileOutputStream fOut = context.openFileOutput(userid, Context.MODE_PRIVATE);
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
            FileInputStream fIn = context.openFileInput(calendar.userid);
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
    void add(CrystalEvent event, Context context)
    {
        event.setStartNotificationIntent(context);
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
        if (realDateList != null) {
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
                    break;
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

}
