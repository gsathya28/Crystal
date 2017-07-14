package com.clairvoyance.crystal;

import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.Calendar;

public class NewEvent extends AppCompatActivity {

    TimePickerDialog.OnTimeSetListener startTimeDialogListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        // Setting up the Set-Time Dialogs
        Button startTimeButton = (Button) findViewById(R.id.startTimeSet);
        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                new TimePickerDialog(NewEvent.this, startTimeDialogListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show();
            }
        });

        Button endTimeButton = (Button) findViewById(R.id.endTimeSet);
        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                new TimePickerDialog(NewEvent.this, startTimeDialogListener, (cal.get(Calendar.HOUR_OF_DAY)) + 1, cal.get(Calendar.MINUTE), false).show();
            }
        });

        //

    }

}
