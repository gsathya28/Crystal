package com.clairvoyance.crystal;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.Calendar;

public class NewEvent extends AppCompatActivity {

    int startYear;
    int startMonth;
    int startDayOfMonth;
    int startHour;
    int startMin;
    int startDisplayHour;
    String MeridiemOfStart;

    int endYear;
    int endMonth;
    int endDayOfMonth;
    int endHour;
    int endMin;
    int endDisplayHour;
    String MeridiemOfEnd;


    TextView startDateTextView;
    TextView endDateTextView;
    TextView startTimeTextView;
    TextView endTimeTextView;


    DatePickerDialog.OnDateSetListener startDateDialogListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            startYear = year;
            startMonth = month;
            startDayOfMonth = dayOfMonth;

            String startDateText = (startMonth + 1) + "/" + startDayOfMonth + "/" + startYear;
            startDateTextView.setText(startDateText);
        }
    };

    DatePickerDialog.OnDateSetListener endDateDialogListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            endYear = year;
            endMonth = month;
            endDayOfMonth = dayOfMonth;

            String endDateText = (endMonth + 1) + "/" + endDayOfMonth + "/" + endYear;
            endDateTextView.setText(endDateText);
        }
    };

    TimePickerDialog.OnTimeSetListener startTimeDialogListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // This runs when the user presses OK in the Dialog box
            startHour = hourOfDay;
            startMin = minute;
            startDisplayHour = (startHour == 0) ? 12 : startHour%12;
            MeridiemOfStart = (startHour >= 12) ? "PM" : "AM";

            String startTimeText = startDisplayHour + ":" + startMin + " " + MeridiemOfStart ;
            startTimeTextView.setText(startTimeText);
        }
    };

    TimePickerDialog.OnTimeSetListener endTimeDialogListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            endHour = hourOfDay;
            endMin = minute;
            endDisplayHour = (endHour == 0) ? 12 : endHour%12;
            MeridiemOfEnd = (endHour >= 12) ? "PM" : "AM";

            String endTimeText = endDisplayHour + ":" + startMin + " " + MeridiemOfEnd;
            endTimeTextView.setText(endTimeText);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        // Setting up default time stuff
        Calendar setCal = Calendar.getInstance();

        startYear = setCal.get(Calendar.YEAR);
        startMonth = setCal.get(Calendar.MONTH);
        startDayOfMonth = setCal.get(Calendar.DAY_OF_MONTH);
        startHour = setCal.get(Calendar.HOUR_OF_DAY);
        startMin = setCal.get(Calendar.MINUTE);
        startDisplayHour = (startHour == 0) ? 12 : startHour%12;

        endYear = setCal.get(Calendar.YEAR);
        endMonth = setCal.get(Calendar.MONTH);
        endDayOfMonth = setCal.get(Calendar.DAY_OF_MONTH);
        endHour = setCal.get(Calendar.HOUR_OF_DAY) + 1;
        endMin = setCal.get(Calendar.MINUTE);
        endDisplayHour = (endHour == 0) ? 12 : endHour%12;

        // Set up Meridiem Stuff
        MeridiemOfStart = (startHour >= 12) ? "PM" : "AM";
        MeridiemOfEnd = (endHour >= 12) ? "PM" : "AM";

        // Displaying default time stuff
        startDateTextView = (TextView) findViewById(R.id.startDate);
        endDateTextView = (TextView) findViewById(R.id.endDate);
        startTimeTextView = (TextView) findViewById(R.id.startTime);
        endTimeTextView = (TextView) findViewById(R.id.endTime);

        String startDateText = (startMonth + 1) + "/" + startDayOfMonth + "/" + startYear;
        startDateTextView.setText(startDateText);
        String endDateText = (endMonth + 1) + "/" + endDayOfMonth + "/" + endYear;
        endDateTextView.setText(endDateText);
        String startTimeText = startDisplayHour + ":" + startMin + " " + MeridiemOfStart;
        startTimeTextView.setText(startTimeText);
        String endTimeText = endDisplayHour + ":" + endMin + " " + MeridiemOfEnd;
        endTimeTextView.setText(endTimeText);

        // Setting up the Set-Time Dialogs through Click Listeners
        Button startTimeButton = (Button) findViewById(R.id.startTimeSet);
        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(NewEvent.this, startTimeDialogListener, startHour, startMin, false).show();
            }
        });

        Button endTimeButton = (Button) findViewById(R.id.endTimeSet);
        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(NewEvent.this, endTimeDialogListener, endHour, endMin, false).show();
            }
        });

        Button startDateButton = (Button) findViewById(R.id.startDateSet);
        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(NewEvent.this, startDateDialogListener, startYear, startMonth, startDayOfMonth).show();
            }
        });

        Button endDateButton = (Button) findViewById(R.id.endDateSet);
        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(NewEvent.this, endDateDialogListener, endYear, endMonth, endDayOfMonth).show();
            }
        });

        // Time to add another Button Listener.
        Button addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

}
