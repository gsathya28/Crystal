package com.clairvoyance.crystal;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by Sathya on 11/26/2017. HI!
 */

class CrystalData implements Serializable {

    private String userid;
    private CrystalCalendar mainCalendar;
    private CrystalData(String id) {userid = id;}

    void save(Context context){
        try {
            FileOutputStream fOut = context.openFileOutput(userid, Context.MODE_PRIVATE);
            fOut.write(Serializer.serialize(this));
            fOut.close();
        } catch (IOException i) {
            Log.d("Save Error: ", i.getMessage());
        }
    }

    static CrystalData read(Context context) {
        CrystalData data = new CrystalData(Build.ID + "@clairvoyance.com");
        try {
            FileInputStream fIn = context.openFileInput(data.userid);
            byte[] byteHolder = new byte[fIn.available()];
            int x = fIn.read(byteHolder);
            if (x != -1)
                data = (CrystalData) Serializer.deserialize(byteHolder);
        } catch (IOException|ClassNotFoundException i)
        {
            Log.d("Read Error: ", i.getMessage());
        }
        return data;
    }

    CrystalCalendar getMainCalendar(){
        return mainCalendar;
    }

}
