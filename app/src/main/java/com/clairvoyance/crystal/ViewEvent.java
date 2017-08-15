package com.clairvoyance.crystal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewEvent extends AppCompatActivity {

    CrystalEvent event;
    Button deleteButton;
    Button editButton;
    Button pushButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        final CrystalCalendar localCalendar = new CrystalCalendar(Build.ID + "@clairvoyance.com");
        CrystalCalendar.read(ViewEvent.this, localCalendar);

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

        editButton = (Button) findViewById(R.id.editEventButton);
        deleteButton = (Button) findViewById(R.id.deleteEventButton);
        pushButton = (Button) findViewById(R.id.pushEventButton);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editEventIntent = new Intent(v.getContext(), EditEvent.class);
                editEventIntent.putExtra("Event", event);
                startActivity(editEventIntent);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewEvent.this);
                builder.setMessage("Are you sure you want to delete the event: " + event.get(CrystalEvent.NAME) + "?");
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        localCalendar.remove(event);
                        Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(mainActivityIntent);
                        localCalendar.save(ViewEvent.this);
                    }
                });

                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                });
                builder.create().show();

            }
        });


    }
}
