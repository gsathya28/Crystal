package com.clairvoyance.crystal;

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



}
