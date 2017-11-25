package com.clairvoyance.crystal;

import java.text.DateFormat;
import java.util.Calendar;

/**
 * Created by Sathya on 11/15/2017.
 */

public class CrystalReminder extends CrystalEvent {

    private Calendar time;
    public CrystalReminder(Calendar inTime, CrystalCalendar localCalendar){
        super(inTime, null, localCalendar);
        time = inTime;
    }

    String displayTimeString(int field, int activity) {
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

}
