package com.clairvoyance.crystal;

import android.content.Context;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Sathya on 11/15/2017. HI!
 */

class CrystalReminder implements CrystalInstant {

    private Calendar time;
    CrystalReminder(Calendar inTime){
        time = inTime;
    }

    public String displayTimeString(int field, int activity) {
        String finalString;
        switch (activity) {
            case VIEW_EVENT:
                switch (field) {
                    case START_TIME:
                        String startDateText = DateFormat.getDateInstance().format(time.getTime());
                        String startTimeText = DateFormat.getTimeInstance(DateFormat.SHORT).format(time.getTime());

                        finalString = startTimeText + "\n" + startDateText;
                        return finalString;
                }
            case AGENDA_VIEW:
                switch (field) {
                    case START_TIME:
                        return DateFormat.getTimeInstance(DateFormat.SHORT).format(time.getTime());
                }
        }

        return "";
    }

    public void setAlarms(Context context, ArrayList<CrystalAlarm> alarms){

    }

    public void setStartNotificationIntent(Context context){

    }

    public boolean set(int field, String string){
        return true;
    }

    public void set(int field, boolean value){

    }

    public Calendar getStartTime(){
        return time;
    }

    public String get(int field)
    {
        return "";
    }

    public Calendar getEndTime(){
        Calendar calendar = (Calendar) time.clone();
        calendar.add(Calendar.HOUR, 1);
        return calendar ;
    }

}
