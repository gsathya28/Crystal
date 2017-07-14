package com.clairvoyance.crystal;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewEvent extends AppCompatActivity {

    TimePickerDialog.OnTimeSetListener startTimeDialogListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm ss", Locale.US);
            Date date = new Date();
            String strDate = timeFormat.format(date);
            Toast.makeText(getBaseContext(), "You have selected " + strDate, Toast.LENGTH_SHORT).show();
        }
    };

    TimePickerDialog.OnTimeSetListener endTimeDialogListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        }
    };

    DatePickerDialog.OnDateSetListener startDateDialogListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        }
    };

    DatePickerDialog.OnDateSetListener endDateDialogListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

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
                new TimePickerDialog(NewEvent.this, endTimeDialogListener, (cal.get(Calendar.HOUR_OF_DAY)) + 1, cal.get(Calendar.MINUTE), false).show();
            }
        });

        Button startDateButton = (Button) findViewById(R.id.startDateSet);
        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                new DatePickerDialog(NewEvent.this, startDateDialogListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Button endDateButton = (Button) findViewById(R.id.endDateSet);
        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                new DatePickerDialog(NewEvent.this, endDateDialogListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

}
