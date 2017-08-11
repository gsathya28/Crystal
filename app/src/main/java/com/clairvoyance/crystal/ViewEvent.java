package com.clairvoyance.crystal;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

public class ViewEvent extends AppCompatActivity {

    CrystalEvent event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        Intent viewEventIntent = getIntent();
        event = (CrystalEvent) viewEventIntent.getSerializableExtra("Event");

        // Toolbar Setup
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar2);
        myToolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        myToolbar.setTitle(event.get(CrystalEvent.NAME));
        setSupportActionBar(myToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        try {
            ab.setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException ne) {
            Log.d("Action Bar", ne.getMessage());
        }

        TextView startTimeTextView = (TextView) findViewById(R.id.startTimeView);
        TextView endTimeTextView = (TextView) findViewById(R.id.endTimeView);

        startTimeTextView.setText(event.displayTimeString(CrystalEvent.START_TIME));
        endTimeTextView.setText(event.displayTimeString(CrystalEvent.END_TIME));

    }
}
