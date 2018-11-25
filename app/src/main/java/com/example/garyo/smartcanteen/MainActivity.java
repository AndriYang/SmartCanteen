package com.example.garyo.smartcanteen;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.SystemClock;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.garyo.smartcanteen.OtherLibraries.CircleProgress;
import com.takusemba.spotlight.OnSpotlightStateChangedListener;
import com.takusemba.spotlight.OnTargetStateChangedListener;
import com.takusemba.spotlight.Spotlight;
import com.takusemba.spotlight.shape.Circle;
import com.takusemba.spotlight.target.SimpleTarget;

import java.util.ArrayList;
//todo implement firebase methods
public class MainActivity extends AppCompatActivity implements MyFirebase {
    CircleProgress circleFillGraph;
    Button ecoButton;
    CardView Table1;
    CardView Table2;
    CardView Table3;
    CardView Table4;
    LinearLayout mainLayout;
    SimpleTarget currentTarget;
    Spotlight currentSpotlight;
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
        circleFillGraph = findViewById(R.id.circle_progress);
        //todo make circle progress live update once firebase is implemented
        /*
        should add up all tables and get filled percentage
         */
        setEcoButton();//sets on click listener and spotlights the 'eco' table
        ArrayList<CardView> tableList = new ArrayList<>();
        tableList.add(Table1);
        tableList.add(Table2);
        tableList.add(Table3);
        tableList.add(Table4);
        setTableListeners(tableList);
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
    private void setEcoButton(){
        ecoButton.setOnClickListener(new View.OnClickListener() {
            //todo dynamically chooses a table base on firebase ->replace all table1 with specified table
            //Currently only picks table 1 in the spotlight
            @Override
            public void onClick(View view) {
                int[] location = new int[2];
                Table1.getLocationOnScreen(location);
                int x = location[0];
                int y = location[1];
                currentTarget = SpotlightHelper.createTarget(MainActivity.this,
                        x,
                        y,
                        new SpotlightHelper()
                                .new RectangleShape(Table1.getWidth(),Table1.getHeight()));
                currentSpotlight = SpotlightHelper.createSpotlight(MainActivity.this,currentTarget);
                currentSpotlight.start();
            }
        });
    }

}

