package com.clairvoyance.crystal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewEvent extends AppCompatActivity {

    CrystalCalendar localCalendar;
    CrystalEvent event;
    Button deleteButton;
    Button editButton;
    Button pushButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        localCalendar = CrystalCalendar.read(ViewEvent.this);

        Intent viewEventIntent = getIntent();
        event = (CrystalEvent) viewEventIntent.getSerializableExtra("Event");

        setToolbar();
        setText();
        setButtons();

    }

    private void setToolbar() {
        // Toolbar Setup
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar2);
        myToolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        myToolbar.setTitle(event.get(CrystalEvent.NAME));
        setSupportActionBar(myToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        if (ab != null) {
            try {
                ab.setDisplayHomeAsUpEnabled(true);
            } catch (NullPointerException ne) {
                Log.d("Action Bar", ne.getMessage());
            }
        }
    }

    private void setText(){
        TextView startTimeTextView = (TextView) findViewById(R.id.startTimeView);
        TextView endTimeTextView = (TextView) findViewById(R.id.endTimeView);

        startTimeTextView.setText(event.displayTimeString(CrystalEvent.START_TIME, CrystalEvent.VIEW_EVENT));
        endTimeTextView.setText(event.displayTimeString(CrystalEvent.END_TIME, CrystalEvent.VIEW_EVENT));

        LinearLayout detailLayout = (LinearLayout) findViewById(R.id.detailLayout);
        TextView notesText = (TextView) findViewById(R.id.view_notes);
        notesText.setText(event.get(CrystalEvent.NOTES));
    }

    private void setButtons(){
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
                        localCalendar.save(ViewEvent.this);
                        Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(mainActivityIntent);

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
