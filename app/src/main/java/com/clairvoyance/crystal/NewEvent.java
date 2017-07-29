package com.clairvoyance.crystal;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.CheckBox;

import java.text.DateFormat;
import java.util.Calendar;


public class NewEvent extends AppCompatActivity {

    Calendar startCalendar;
    Calendar endCalendar;

    LinearLayout startDateRow;
    LinearLayout startTimeRow;
    LinearLayout endDateRow;
    LinearLayout endTimeRow;
    TextView startDateTextView;
    TextView endDateTextView;
    TextView startTimeTextView;
    TextView endTimeTextView;
    TextView startDateTitleView;
    TextView startTimeTitleView;
    TextView endDateTitleView;
    TextView endTimeTitleView;
    CheckBox reminderCheck;
    CheckBox allDayCheck;
    CheckBox multipleDayCheck;

    boolean eventInPast;

    DatePickerDialog.OnDateSetListener startDateDialogListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            startCalendar.set(Calendar.YEAR, year);
            startCalendar.set(Calendar.MONTH, month);
            startCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            eventInPast = (startCalendar.before(Calendar.getInstance()) && endCalendar.before(Calendar.getInstance()));

            if (!startBeforeEnd())
            {
                endCalendar = (Calendar) startCalendar.clone();
                endCalendar.add(Calendar.HOUR_OF_DAY, +1);
                String endDateText = DateFormat.getDateInstance().format(endCalendar.getTime());
                endDateTextView.setText(endDateText);
                String endTimeText = DateFormat.getTimeInstance(DateFormat.SHORT).format(endCalendar.getTime());
                endTimeTextView.setText(endTimeText);
            }

            String startDateText = DateFormat.getDateInstance().format(startCalendar.getTime());
            startDateTextView.setText(startDateText);
            String startTimeText = DateFormat.getTimeInstance(DateFormat.SHORT).format(startCalendar.getTime());
            startTimeTextView.setText(startTimeText);
        }
    };

    DatePickerDialog.OnDateSetListener endDateDialogListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            endCalendar.set(Calendar.YEAR, year);
            endCalendar.set(Calendar.MONTH, month);
            endCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            eventInPast = (startCalendar.before(Calendar.getInstance()) && endCalendar.before(Calendar.getInstance()));

            if (!startBeforeEnd())
            {
                endCalendar = (Calendar) startCalendar.clone();
                endCalendar.add(Calendar.HOUR_OF_DAY, 1);
                getEndBeforeStartAlert("End Date").show();
            }

            String endDateText = DateFormat.getDateInstance().format(endCalendar.getTime());
            endDateTextView.setText(endDateText);
            String endTimeText = DateFormat.getTimeInstance(DateFormat.SHORT).format(endCalendar.getTime());
            endTimeTextView.setText(endTimeText);
        }
    };

    TimePickerDialog.OnTimeSetListener startTimeDialogListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // This runs when the user presses OK in the Dialog box
            startCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            startCalendar.set(Calendar.MINUTE, minute);

            eventInPast = (startCalendar.before(Calendar.getInstance()) && endCalendar.before(Calendar.getInstance()));

            if (!startBeforeEnd())
            {
                endCalendar = (Calendar) startCalendar.clone();
                endCalendar.add(Calendar.HOUR_OF_DAY, +1);
                String endDateText = DateFormat.getDateInstance().format(endCalendar.getTime());
                endDateTextView.setText(endDateText);
                String endTimeText = DateFormat.getTimeInstance(DateFormat.SHORT).format(endCalendar.getTime());
                endTimeTextView.setText(endTimeText);
            }

            String startDateText = DateFormat.getDateInstance().format(startCalendar.getTime());
            startDateTextView.setText(startDateText);
            String startTimeText = DateFormat.getTimeInstance(DateFormat.SHORT).format(startCalendar.getTime());
            startTimeTextView.setText(startTimeText);
        }
    };

    TimePickerDialog.OnTimeSetListener endTimeDialogListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            endCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            endCalendar.set(Calendar.MINUTE, minute);

            eventInPast = (startCalendar.before(Calendar.getInstance()) && endCalendar.before(Calendar.getInstance()));

            if (!startBeforeEnd())
            {
                getEndBeforeStartAlert("End Time").show();
            }

            String endDateText = DateFormat.getDateInstance().format(endCalendar.getTime());
            endDateTextView.setText(endDateText);
            String endTimeText = DateFormat.getTimeInstance(DateFormat.SHORT).format(endCalendar.getTime());
            endTimeTextView.setText(endTimeText);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        // Toolbar Setup
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        myToolbar.setTitle("New Event");
        setSupportActionBar(myToolbar);

        // Dialog Setup

        // Setting up default time stuff
        startCalendar = Calendar.getInstance();
        endCalendar = (Calendar) startCalendar.clone();
        endCalendar.add(Calendar.HOUR_OF_DAY, 1);

        // Extracting all the views
        startDateTextView = (TextView) findViewById(R.id.startDate);
        endDateTextView = (TextView) findViewById(R.id.endDate);
        startTimeTextView = (TextView) findViewById(R.id.startTime);
        endTimeTextView = (TextView) findViewById(R.id.endTime);
        startDateTitleView = (TextView) findViewById(R.id.startDateTitle);
        startTimeTitleView = (TextView) findViewById(R.id.startTimeTitle);
        endDateTitleView = (TextView) findViewById(R.id.endDateTitle);
        endTimeTitleView = (TextView) findViewById(R.id.endTimeTitle);
        reminderCheck = (CheckBox) findViewById(R.id.reminderCheckBox);
        allDayCheck = (CheckBox) findViewById(R.id.allDayCheckBox);
        multipleDayCheck = (CheckBox) findViewById(R.id.multipleDayCheckBox);
        startDateRow = (LinearLayout) findViewById(R.id.startDateRow);
        startTimeRow = (LinearLayout) findViewById(R.id.startTimeRow);
        endDateRow = (LinearLayout) findViewById(R.id.endDateRow);
        endTimeRow = (LinearLayout) findViewById(R.id.endTimeRow);

        endDateRow.setVisibility(View.GONE);

        // Extracting all the Buttons
        Button startTimeButton = (Button) findViewById(R.id.startTimeSet);
        Button endTimeButton = (Button) findViewById(R.id.endTimeSet);
        Button startDateButton = (Button) findViewById(R.id.startDateSet);
        Button endDateButton = (Button) findViewById(R.id.endDateSet);

        // Displaying default time stuff
        String startDateText = DateFormat.getDateInstance().format(startCalendar.getTime());
        startDateTextView.setText(startDateText);
        String endDateText = DateFormat.getDateInstance().format(endCalendar.getTime());
        endDateTextView.setText(endDateText);
        String startTimeText = DateFormat.getTimeInstance(DateFormat.SHORT).format(startCalendar.getTime());
        startTimeTextView.setText(startTimeText);
        String endTimeText = DateFormat.getTimeInstance(DateFormat.SHORT).format(endCalendar.getTime());
        endTimeTextView.setText(endTimeText);

        // Setting up the Set-Time Dialogs through Click Listeners
        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(NewEvent.this, startTimeDialogListener, startCalendar.get(Calendar.HOUR_OF_DAY), startCalendar.get(Calendar.MINUTE), false).show();
            }
        });


        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(NewEvent.this, endTimeDialogListener, endCalendar.get(Calendar.HOUR_OF_DAY), endCalendar.get(Calendar.MINUTE), false).show();
            }
        });


        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(NewEvent.this, startDateDialogListener, startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(NewEvent.this, endDateDialogListener, endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Reminder Checkbox Listener
        reminderCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    allDayCheck.setChecked(false);
                    multipleDayCheck.setChecked(false);
                    endTimeRow.setVisibility(View.GONE);
                    startTimeTitleView.setText(R.string.time);
                    startTimeRow.setVisibility(View.VISIBLE);
                }
                else
                {
                    endTimeRow.setVisibility(View.VISIBLE);
                    startTimeTitleView.setText(R.string.start_time);
                }
            }
        });
        allDayCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    reminderCheck.setChecked(false);
                    startTimeRow.setVisibility(View.GONE);
                    endTimeRow.setVisibility(View.GONE);
                }
                else
                {
                    startTimeRow.setVisibility(View.VISIBLE);
                    endTimeRow.setVisibility(View.VISIBLE);

                }
            }
        });

        multipleDayCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    reminderCheck.setChecked(false);
                    startDateTitleView.setText(R.string.start_date);
                    endDateRow.setVisibility(View.VISIBLE);
                }
                else
                {
                    startDateTitleView.setText(R.string.date);
                    endDateRow.setVisibility(View.GONE);
                }
            }
        });


        // Time to add the Add-Event Button Listener.
        Button addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    AlertDialog getEndBeforeStartAlert(String setType)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewEvent.this);

        builder.setMessage("The " + setType + " you have entered is currently before your start time (" + DateFormat.getDateInstance().format(startCalendar.getTime()) + " " + DateFormat.getTimeInstance(DateFormat.SHORT).format(startCalendar.getTime()) + ") Do you wish to continue?");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        AlertDialog endBeforeStartDialog = builder.create();
        return endBeforeStartDialog;
    }

    protected boolean startBeforeEnd()
    {
        return startCalendar.before(endCalendar);
    }

}
