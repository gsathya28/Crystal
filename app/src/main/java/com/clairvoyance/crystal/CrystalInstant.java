package com.clairvoyance.crystal;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Sathya on 11/26/2017. HI!
 */

interface CrystalInstant extends Serializable {

    int ALL_DAY = 0;
    int IS_REMINDER = 1;
    int NAME = 2;
    int NOTES = 3;
    int START_TIME = 4;
    int END_TIME = 5;
    int ID = 6;
    int AGENDA_VIEW = 7;
    int VIEW_EVENT = 8;

    String displayTimeString(int field, int activity);
    void setStartNotificationIntent(Context context);
    void setAlarms(Context context, ArrayList<CrystalAlarm> crystalAlarms);
    boolean set(int field, String string);
    void set(int field, boolean value);
    Calendar getStartTime();
    String get(int field);
    Calendar getEndTime();
    boolean isBefore(CrystalInstant instant);

}
