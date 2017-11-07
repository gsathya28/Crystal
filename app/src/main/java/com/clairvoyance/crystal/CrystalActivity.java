package com.clairvoyance.crystal;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Sathya on 10/14/2017.
 * Yup!
 */

public abstract class CrystalActivity extends AppCompatActivity {
    protected abstract void setData();
    protected abstract void setStaticGUI();
    protected abstract void setDynamicGUI();
}
