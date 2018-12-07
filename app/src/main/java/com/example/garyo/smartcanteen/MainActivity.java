package com.example.garyo.smartcanteen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.garyo.smartcanteen.OtherLibraries.CircleProgress;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.takusemba.spotlight.Spotlight;
import com.takusemba.spotlight.target.SimpleTarget;

import java.util.ArrayList;
import java.util.HashMap;

//todo implement firebase methods
public class MainActivity extends AppCompatActivity {
    CircleProgress circleFillGraph;
    Button ecoButton;
    CardView Table1,Table2,Table3,Table4;
    LinearLayout mainLayout;
    SimpleTarget currentTarget;
    Spotlight currentSpotlight;
    Firebase mRef,currentRef;
    int totalNumberOfPeople;
    SharedPreferences prefs;
    int capacity;
    HashMap<String,View> tableMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        mainLayout = findViewById(R.id.main_layout);

        ecoButton = findViewById(R.id.eco_button);
        Table1 = findViewById(R.id.table1_card);
        Table2 = findViewById(R.id.table2_card);
        Table3 = findViewById(R.id.table3_card);
        Table4 = findViewById(R.id.table4_card);

        tableMap = new HashMap<>();
        tableMap.put("Table 1",Table1);
        tableMap.put("Table 2",Table2);
        tableMap.put("Table 3",Table3);
        tableMap.put("Table 4",Table4);

        circleFillGraph = findViewById(R.id.circle_progress);

        //allows user to set canteen capacity default to 100
        prefs = getSharedPreferences("filename",MODE_PRIVATE);
        capacity = prefs.getInt(Settings.CAPACITY_KEY,100);

        Log.i("prefs",capacity+"");

        ArrayList<CardView> tableList = new ArrayList<>();

        tableList.add(Table1);
        tableList.add(Table2);
        tableList.add(Table3);
        tableList.add(Table4);
        setTableListeners(tableList);

        mRef = new Firebase(getResources().getString(R.string.firebase_url));
        currentRef = mRef.child("current");


        //gets total number of people and updates circle graph
        currentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                totalNumberOfPeople = 0;
                View minView = null;
                 for(DataSnapshot ds : dataSnapshot.getChildren()){
                     Log.i("firebaseM", "key: " + ds.getKey()
                             + " value: " + ds.getValue());
                     try{
                         int x = Integer.parseInt(ds.getValue().toString());

                         //get minimum and setting on click listener
                         int min = 9999999;
                         if (x<min){
                             min = x;
                             minView = tableMap.get(ds.getKey().toString());
                         }
                         //get total
                         totalNumberOfPeople+= x;
                         circleFillGraph.setProgress(totalNumberOfPeople*100/capacity);
                     }catch (NumberFormatException e){
                         e.printStackTrace();
                     }catch(ArithmeticException e){
                         e.printStackTrace();
                         capacity = 100;
                     }
                 }
                 Log.i("test2",minView.toString());
                 setEcoButton(minView);
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    //Sends this to the table activity with a graph
    public void setTableListeners(ArrayList<CardView> table){
        for(final CardView cardview:table){
            cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this,TableActivity.class);
                    intent.putExtra("table",((TextView)cardview.findViewById(R.id.table_name)).getText().toString());
                    //this puts the table name from the card into the extra
                    startActivity(intent);
                }
            });
        }
    }


    //this inflates the menu from res/menu/menu_main
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //in case we want to add more menus
        switch (item.getItemId()) {
            case R.id.settings_activity:
                Intent intent = new Intent(this,Settings.class);
                startActivity(intent);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    //this is done so that the code looks neater
    private void setEcoButton(View min){
        final View minView = min;
        ecoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] location = new int[2];
                minView.getLocationOnScreen(location);
                int x = location[0];
                int y = location[1];
                currentTarget = SpotlightHelper.createTarget(MainActivity.this,
                        x,
                        y,
                        new SpotlightHelper()
                                .new RectangleShape(minView.getWidth(),minView.getHeight()));
                currentSpotlight = SpotlightHelper.createSpotlight(MainActivity.this,currentTarget);
                currentSpotlight.start();
            }
        });
    }

}

