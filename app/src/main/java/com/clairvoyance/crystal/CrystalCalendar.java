package com.clairvoyance.crystal;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sathya on 8/9/2017.
 * Sweet!
 */

class CrystalCalendar implements Serializable{

    private String userid;
    private ArrayList<CrystalEvent> crystalEvents = new ArrayList<>();

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
        crystalEvents.add(event);
    }

    ArrayList<CrystalEvent> getEvents()
    {
        return crystalEvents;
    }

}
