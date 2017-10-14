package com.clairvoyance.crystal;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by Sathya on 8/12/2017.
 * GUI Docs
 */

class CrystalGUI {

    private final static int LAYOUT = 0;
    final static int FONT = 1;

    public CrystalGUI() {

    }

    static int getFontSizeInPx(Context context, int value, int type)
    {
        Resources r = context.getResources();
        switch (type){
            case FONT:
                return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, r.getDisplayMetrics());
            case LAYOUT:
                return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, r.getDisplayMetrics());
        }

        return 0;
    }

}
