package com.clairvoyance.crystal;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

// Todo: Fix Back Button Problems
public class MainActivity extends CrystalActivity {

    private TextView mTextMessage;
    CrystalCalendar localCalendar;
    ArrayList<ArrayList<CrystalEvent>> eventList;
    boolean savedCalendarOnFile = false;
    boolean isAgendaActive = false;
    LinearLayout focusDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setData();
        setStaticGUI();
        setDynamicGUI();
    }

    @Override
    protected void setData() {
        // Todo: Put this on separate Thread

        // Check for saved calendar
        Log.d("Calendar Stats", "Calendar Created not Written");
        localCalendar = CrystalCalendar.read(this);
        eventList = localCalendar.getEvents();
        savedCalendarOnFile = !(eventList.isEmpty());
    }

    @Override
    protected void setStaticGUI() {
        setToolbar();
    }

    @Override
    protected void setDynamicGUI() {
        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
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
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_agenda);

        setScrollbar();
    }


    private void createAgenda() {
        if (!isAgendaActive) {

            isAgendaActive = true;

            // If eventList.size() == 0

            if (savedCalendarOnFile) {
                LinearLayout outerLinearLayout = (LinearLayout) findViewById(R.id.mainLayout);
                focusDate = CrystalGUI.generateOuterAgendaLinearLayout(MainActivity.this, outerLinearLayout, eventList);

            } else {
                // Generate A block of code saying that a new event should be made yo!
                Log.d("GUI Stats", "No Events!");
            }


        }
    }


    private void setToolbar() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(myToolbar);
    }

    private void setScrollbar(){
        ScrollView scrollView = (ScrollView) findViewById(R.id.mainScroll);
        if (focusDate != null){
            boolean result = scrollView.fullScroll(View.FOCUS_DOWN);
            if (result){
                Log.d("Scroll:", "True");
            }
            else{
                Log.d("Scroll:", "False");
            }

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
                newEventIntent.putExtra("Local_Calendar", localCalendar);
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
