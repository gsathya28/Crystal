package com.clairvoyance.crystal;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    Calendar localCalendar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_agenda:
                    mTextMessage.setText(R.string.agenda);
                    createAgenda();
                    return true;
                case R.id.navigation_weekly:
                    mTextMessage.setText(R.string.weekly);
                    return true;
                case R.id.navigation_monthly:
                    mTextMessage.setText(R.string.monthly);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Check for local Calendar on Phone
        // Todo: Put this on separate Thread
        try {
            FileInputStream fin = new FileInputStream("localCalendar.ics");
            CalendarBuilder builder = new CalendarBuilder();
            localCalendar = builder.build(fin);
        }
        catch (IOException in) {
            localCalendar = new net.fortuna.ical4j.model.Calendar();
            localCalendar.getProperties().add(new ProdId("-//Sathya Govindarajan//iCal4j 2.0.0//EN"));
            localCalendar.getProperties().add(Version.VERSION_2_0);
            localCalendar.getProperties().add(CalScale.GREGORIAN);

            try {
                FileOutputStream fOut = new FileOutputStream("localCalendar.ics");
                CalendarOutputter outPutter = new CalendarOutputter();
                outPutter.output(localCalendar, fOut);
            } catch (IOException|ValidationException out) {
                Log.d("Save Error: ", out.getMessage());
            }
        }
        catch (ParserException parseErr) {
            Log.d("Read Error: ", parseErr.getMessage());
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(myToolbar);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_agenda);
    }

    private void createAgenda() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.mainLayout);

        String[] textArray = {"Event One", "Event Two", "Event Three", "Event Four"};

        for( int i = 0; i < textArray.length; i++ )
        {
            Button eventButton = new Button(this);

            // Formatting the TextView - Margins
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );

            int topValueInPx = (int) getResources().getDimension(R.dimen.activity_vertical_margin);
            int bottomValueInPx = (int) getResources().getDimension(R.dimen.activity_vertical_margin);
            bottomValueInPx = bottomValueInPx / 2;
            int leftValueInPx = (int) getResources().getDimension(R.dimen.activity_horizontal_margin);

            if (i != 0)
            {
                topValueInPx = topValueInPx / 2;
            }

            params.setMargins(leftValueInPx, topValueInPx, leftValueInPx, bottomValueInPx);
            eventButton.setLayoutParams(params);

            // Formatting the TextView - Background and Text Color
            eventButton.setTextColor(Color.parseColor("#FFFFFF"));
            eventButton.setBackgroundColor(Color.parseColor("#0000FF"));

            // Formatting Font -
            Context mContext = MainActivity.this;
            Resources r = mContext.getResources();

            int fontSizeInPx = (int) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, 12, r.getDisplayMetrics());
            eventButton.setTextSize(fontSizeInPx);

            // Formatting Button - Text Alignment
            int topPaddingAdjustment = 5;
            int leftPaddingAdjustment = 16;
            eventButton.setGravity(3);
            eventButton.setPadding(
                    eventButton.getPaddingLeft() + leftPaddingAdjustment,
                    eventButton.getPaddingTop() + topPaddingAdjustment,
                    eventButton.getPaddingRight() - leftPaddingAdjustment,
                    eventButton.getPaddingBottom() - topPaddingAdjustment
            );


            // Adding the TextView to the Layout
            eventButton.setText(textArray[i]);
            linearLayout.addView(eventButton);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_add_event:
                Intent newEventIntent = new Intent(this, NewEvent.class);
                startActivity(newEventIntent);
                return true;

            case R.id.action_search_event:
                super.onOptionsItemSelected(item);
                return true;

            case R.id.action_date_select:

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

}
