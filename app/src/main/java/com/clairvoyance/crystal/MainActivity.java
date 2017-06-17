package com.clairvoyance.crystal;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_agenda:
                    mTextMessage.setText("Agenda");
                    createAgenda();
                    return true;
                case R.id.navigation_weekly:
                    mTextMessage.setText("Weekly");
                    return true;
                case R.id.navigation_monthly:
                    mTextMessage.setText("Monthly");
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_agenda);
    }

    private void createAgenda()
    {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.mainLayout);

        String[] textArray = {"One", "Two", "Three", "Four"};

        for( int i = 0; i < textArray.length; i++ )
        {
            TextView textView = new TextView(this);
            textView.setText(textArray[i]);
            // Formatting the TextView
            /*
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );


            Resources r = mContext.getResources();
            int topbottomPx = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    R.dimen.activity_vertical_margin,
                    r.getDisplayMetrics()
            );
            params.setMargins(R.dimen.activity_horizontal_margin, R.dimen.activity_vertical_margin, R.dimen.activity_horizontal_margin, R.dimen.activity_vertical_margin);

            textView.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            textView.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);

            */

            linearLayout.addView(textView);
        }
    }

}
