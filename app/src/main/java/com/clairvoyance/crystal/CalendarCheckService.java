package com.clairvoyance.crystal;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Sathya on 8/16/2017.
 *
 */



public class CalendarCheckService extends IntentService {


    public CalendarCheckService()
    {
        super("myNewCalendarCheckService");
    }

    @Override
    protected void onHandleIntent(Intent workIntent)
    {
        // Gets data from the incoming Intent
        // String dataString = workIntent.getDataString();



    }
}
