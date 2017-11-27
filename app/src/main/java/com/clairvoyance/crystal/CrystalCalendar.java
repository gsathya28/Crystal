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
    private ArrayList<ArrayList<CrystalInstant>> crystalEvents = new ArrayList<>();
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
     * Adds an event to the list of events in the calendar sorted by date of the start time
     *
     * @param event the event to be added
     */
    void add(CrystalInstant event, Context context)
    {
        event.setStartNotificationIntent(context);
        // Todo: Add based on start Date - for check
        eventCount++;
        // If empty, just add the event in a new date group
        if (crystalEvents.isEmpty())
        {
            ArrayList<CrystalInstant> newDateList = new ArrayList<>();
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
        ArrayList<CrystalInstant> realDateList = findDate(event);
        // If the date group is found:
        if (realDateList != null) {
            for (CrystalInstant dateEvent : realDateList)
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
            ArrayList<CrystalInstant> newDateList = new ArrayList<>();
            newDateList.add(event);

            for (ArrayList<CrystalInstant> dateList : crystalEvents) {
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
    boolean remove(CrystalInstant event)
    {
        ArrayList<CrystalInstant> dateList = findDate(event);
        if (dateList != null) {

            int index = crystalEvents.indexOf(dateList);
            crystalEvents.remove(dateList);
            Iterator<CrystalInstant> i = dateList.iterator();

            while (i.hasNext())
            {
                CrystalInstant eventCheck = i.next();
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
    private ArrayList<CrystalInstant> findDate(CrystalInstant event)
    {
        Calendar startCalendar = event.getStartTime();
        Calendar strippedStartCalendar = (Calendar) startCalendar.clone();
        strippedStartCalendar.set(startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DATE), 0, 0, 0);
        strippedStartCalendar.set(Calendar.MILLISECOND, 0);

        for (ArrayList<CrystalInstant> dateList : crystalEvents)
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
    ArrayList<ArrayList<CrystalInstant>> getEvents()
    {
        return crystalEvents;
    }

}
