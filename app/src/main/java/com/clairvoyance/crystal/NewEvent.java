package com.clairvoyance.crystal;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.CheckBox;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
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
    Spinner pushNotifTimeTypeSpinner;

    CrystalCalendar localCalendar;
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
                endCalendar = (Calendar) startCalendar.clone();
                endCalendar.add(Calendar.HOUR_OF_DAY, +1);
                AlertDialog endBeforeStart = getEndBeforeStartAlert();
                endBeforeStart.show();
            }

            String endDateText = DateFormat.getDateInstance().format(endCalendar.getTime());
            endDateTextView.setText(endDateText);
            String endTimeText = DateFormat.getTimeInstance(DateFormat.SHORT).format(endCalendar.getTime());
            endTimeTextView.setText(endTimeText);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Setup Local Calendar
        localCalendar = (CrystalCalendar) getIntent().getSerializableExtra("Local_Calendar");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        setToolbar();
        setCalendars();
        setText();
        setCheckBoxes();
        setSpinner();
        setButtons();
    }

    AlertDialog getEndBeforeStartAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewEvent.this);

        builder.setMessage("The time you have entered is before your start time (" + DateFormat.getDateInstance().format(startCalendar.getTime()) + " " + DateFormat.getTimeInstance(DateFormat.SHORT).format(startCalendar.getTime()) + ").");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                TimePickerDialog endTimeSelect = new TimePickerDialog(NewEvent.this, endTimeDialogListener, endCalendar.get(Calendar.HOUR_OF_DAY), endCalendar.get(Calendar.MINUTE), false);
                String startTimeTitle = getResources().getString(R.string.start_time);
                if (!multipleDayCheck.isChecked()) {
                    String startTimeText = DateFormat.getTimeInstance(DateFormat.SHORT).format(startCalendar.getTime());
                    endTimeSelect.setTitle(startTimeTitle + "\n" + startTimeText);
                }
                else {
                    String startTimeText = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(startCalendar.getTime());
                    String endDateTitle = getResources().getString(R.string.end_date);
                    String endDateText = DateFormat.getDateInstance(DateFormat.MEDIUM).format(endCalendar.getTime());
                    endTimeSelect.setTitle(startTimeTitle + "\n" + startTimeText + "\n" + endDateTitle + "\n" + endDateText);
                }
                endTimeSelect.show();
            }
        });

        return builder.create();
    }

    protected boolean startBeforeEnd()
    {
        return startCalendar.before(endCalendar);
    }

    protected void setToolbar() {
        // Toolbar Setup
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        myToolbar.setTitle("New Event");
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

    protected void setCalendars() {
        // Setting up default time stuff
        startCalendar = Calendar.getInstance();

        // Set the minutes right!
        int minute = startCalendar.get(Calendar.MINUTE);
        switch (minute / 15)
        {
            case 0:
                if (minute != 0)
                    startCalendar.set(Calendar.MINUTE, 15);
                break;
            case 1:
                if (minute != 15)
                    startCalendar.set(Calendar.MINUTE, 30);
                break;
            case 2:
                if (minute != 30)
                    startCalendar.set(Calendar.MINUTE, 45);
                break;
            case 3:
                if (minute != 45) {
                    startCalendar.add(Calendar.HOUR_OF_DAY, 1);
                    startCalendar.set(Calendar.MINUTE, 0);
                }
                break;
        }
        startCalendar.set(Calendar.SECOND, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);

        endCalendar = (Calendar) startCalendar.clone();
        endCalendar.add(Calendar.HOUR_OF_DAY, 1);


    }

    protected void setText() {
        // Extracting all the views
        startDateTextView = (TextView) findViewById(R.id.startDate);
        endDateTextView = (TextView) findViewById(R.id.endDate);
        startTimeTextView = (TextView) findViewById(R.id.startTime);
        endTimeTextView = (TextView) findViewById(R.id.endTime);
        startDateTitleView = (TextView) findViewById(R.id.startDateTitle);
        startTimeTitleView = (TextView) findViewById(R.id.startTimeTitle);
        endDateTitleView = (TextView) findViewById(R.id.endDateTitle);
        endTimeTitleView = (TextView) findViewById(R.id.endTimeTitle);

        startDateRow = (LinearLayout) findViewById(R.id.startDateRow);
        startTimeRow = (LinearLayout) findViewById(R.id.startTimeRow);
        endDateRow = (LinearLayout) findViewById(R.id.endDateRow);
        endTimeRow = (LinearLayout) findViewById(R.id.endTimeRow);

        // Displaying default time stuff
        String startDateText = DateFormat.getDateInstance().format(startCalendar.getTime());
        startDateTextView.setText(startDateText);
        String endDateText = DateFormat.getDateInstance().format(endCalendar.getTime());
        endDateTextView.setText(endDateText);
        String startTimeText = DateFormat.getTimeInstance(DateFormat.SHORT).format(startCalendar.getTime());
        startTimeTextView.setText(startTimeText);
        String endTimeText = DateFormat.getTimeInstance(DateFormat.SHORT).format(endCalendar.getTime());
        endTimeTextView.setText(endTimeText);

        endDateRow.setVisibility(View.GONE);
    }

    protected void setButtons() {
        // Extracting all the Buttons
        Button startTimeButton = (Button) findViewById(R.id.startTimeSet);
        Button endTimeButton = (Button) findViewById(R.id.endTimeSet);
        Button startDateButton = (Button) findViewById(R.id.startDateSet);
        Button endDateButton = (Button) findViewById(R.id.endDateSet);

        // Setting up the Set-Time Dialogs through Click Listeners
        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(NewEvent.this, R.style.Theme_AppCompat_DayNight_Dialog, startTimeDialogListener, startCalendar.get(Calendar.HOUR_OF_DAY), startCalendar.get(Calendar.MINUTE), false).show();
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
                DatePickerDialog startDateSelect = new DatePickerDialog(NewEvent.this, startDateDialogListener, startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH));
                startDateSelect.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis() + 1000);
                startDateSelect.setTitle("");
                startDateSelect.show();
            }
        });

        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog endDateSelect = new DatePickerDialog(NewEvent.this, endDateDialogListener, endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DAY_OF_MONTH));
                endDateSelect.getDatePicker().setMinDate(startCalendar.getTimeInMillis() + 1000);
                endDateSelect.setTitle("");
                endDateSelect.show();
            }
        });

        Button addEventButton = (Button) findViewById(R.id.addButton);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Todo: Put on separate Thread. - Load Calendar and Insert Event

                CrystalEvent newEvent = new CrystalEvent(startCalendar, endCalendar, localCalendar);

                TextView eventNameTextView = (TextView) findViewById(R.id.eventName);
                String eventName = eventNameTextView.getText().toString();
                if (!newEvent.set(CrystalEvent.NAME, eventName))
                {
                    Toast.makeText(NewEvent.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

                newEvent.set(CrystalEvent.ALL_DAY, allDayCheck.isChecked());

                localCalendar.add(newEvent, getApplicationContext());
                localCalendar.save(NewEvent.this);

                Intent localSave = new Intent(getApplicationContext(), MainActivity.class);
                localSave.putExtra("new_event", newEvent);
                startActivity(localSave);
            }
        });

        Button addNotifButton = (Button) findViewById(R.id.add_notif);
        addNotifButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainNewLayout);

                final LinearLayout newNotifRow = new LinearLayout(NewEvent.this);
                newNotifRow.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                newNotifRow.setLayoutParams(layoutParams);

                EditText notifNumber = new EditText(NewEvent.this);
                Spinner notifTypes = new Spinner(NewEvent.this);
                Button deleteOption = new Button(NewEvent.this);

                newNotifRow.addView(notifNumber);
                newNotifRow.addView(notifTypes);
                newNotifRow.addView(deleteOption);
                mainLayout.addView(newNotifRow, mainLayout.getChildCount() - 1);

                // NotifNumber Formatting
                int leftValueInPx = (int) (getResources().getDimension(R.dimen.activity_vertical_margin) / 2.);
                LinearLayout.LayoutParams notifNumberLayout = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2);
                notifNumberLayout.setMargins(leftValueInPx, 0, 0, 0);
                notifNumber.setLayoutParams(notifNumberLayout);

                notifNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                notifNumber.setPadding(notifNumber.getPaddingLeft(), 0, notifNumber.getPaddingRight(), 0);
                notifNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
                notifNumber.setGravity(Gravity.CENTER);
                notifNumber.setBackgroundResource(R.drawable.border_black);

                // NotifType Formatting + Data
                LinearLayout.LayoutParams notifTypeLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
                notifTypes.setLayoutParams(notifTypeLayout);

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(NewEvent.this, R.array.timeMeasureOptions, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                notifTypes.setAdapter(adapter);

                // Delete Button Formatting + Listener
                deleteOption.setText(R.string.del);
                deleteOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mainLayout.removeView(newNotifRow);
                    }
                });
            }
        });
    }

    /* Todo: Make sure all the calendars are modified when the checkboxes are checked/unchecked
            Not just the textViews
    */
    public void setCheckBoxes() {
        reminderCheck = (CheckBox) findViewById(R.id.reminderCheckBox);
        allDayCheck = (CheckBox) findViewById(R.id.allDayCheckBox);
        multipleDayCheck = (CheckBox) findViewById(R.id.multipleDayCheckBox);

        reminderCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    allDayCheck.setChecked(false);
                    multipleDayCheck.setChecked(false);
                    endTimeRow.setVisibility(View.GONE);
                    startTimeTitleView.setText(R.string.time);
                    startTimeRow.setVisibility(View.VISIBLE);
                }
                else {
                    endTimeRow.setVisibility(View.VISIBLE);
                    startTimeTitleView.setText(R.string.start_time);
                }
            }
        });
        allDayCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    reminderCheck.setChecked(false);
                    startTimeRow.setVisibility(View.GONE);
                    endTimeRow.setVisibility(View.GONE);
                }
                else {
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
                    endCalendar = (Calendar) startCalendar.clone();
                    endCalendar.add(Calendar.HOUR_OF_DAY, 1);

                    String endDateText = DateFormat.getDateInstance().format(endCalendar.getTime());
                    endDateTextView.setText(endDateText);
                    String endTimeText = DateFormat.getTimeInstance(DateFormat.SHORT).format(endCalendar.getTime());
                    endTimeTextView.setText(endTimeText);
                }
            }
        });

        Calendar checkCalendar = Calendar.getInstance();
        checkCalendar.set(Calendar.HOUR_OF_DAY, 23);
        if (startCalendar.get(Calendar.HOUR_OF_DAY) == checkCalendar.get(Calendar.HOUR_OF_DAY))
        {
            multipleDayCheck.setChecked(true);
        }

    }

    public void setSpinner() {
        pushNotifTimeTypeSpinner = (Spinner) findViewById(R.id.pushNotificationTypes);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.timeMeasureOptions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pushNotifTimeTypeSpinner.setAdapter(adapter);
    }

    // Todo: Finish this function
    private ArrayList<CrystalAlarm> getAlarmsFromLayout(){
        ArrayList<CrystalAlarm> alarms = new ArrayList<>();
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainNewLayout);
        LinearLayout lastLayout = (LinearLayout) mainLayout.getChildAt(mainLayout.getChildCount() - 1);
        return alarms;
    }

}
