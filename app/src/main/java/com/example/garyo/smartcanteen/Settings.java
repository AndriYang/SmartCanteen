package com.example.garyo.smartcanteen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
//todo set capacity and smart reminder and any other settings
//todo use sharedPreferences
/*
my plan here is to add the capacity of the table as shared preferences and
also a reminder smart reminder that alerts the user when the canteeen is not crowded
percentage of "crowdedness" should be set by user
 */
public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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
}
