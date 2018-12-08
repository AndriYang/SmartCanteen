package com.example.garyo.smartcanteen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/*
my plan here is to add the capacity of the table as shared preferences and
also a reminder smart reminder that alerts the user when the canteeen is not crowded
percentage of "crowdedness" should be set by user
 */
public class Settings extends AppCompatActivity {
    public static final String CAPACITY_KEY = "capacity";
    EditText setCapacityET, notiLimit;
    Button setButton, notiButton;
    SharedPreferences prefs;
    int capacity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setCapacityET = findViewById(R.id.settingsET);
        setButton = findViewById(R.id.set_button);
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    capacity = Integer.parseInt(setCapacityET.getText().toString());
                    Intent intent = new Intent(Settings.this, MainActivity.class);
                    startActivity(intent);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });
        notiLimit = findViewById(R.id.noti_limit);
        notiButton = findViewById(R.id.notification_button);
        notiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int limit = Integer.parseInt(notiLimit.getText().toString());
                    Intent intent = new Intent(Settings.this, SmartNotification.class);
                    intent.putExtra("limit", limit);
                    startService(intent);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //this is to ensure same transition animation between back button
                // and back button on tool bar
                finishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences("filename", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putInt(CAPACITY_KEY, capacity);
        Log.i("pref", capacity + "");
        prefsEditor.apply();
    }
}
