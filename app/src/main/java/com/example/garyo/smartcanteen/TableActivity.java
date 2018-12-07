package com.example.garyo.smartcanteen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TableActivity extends AppCompatActivity {
    //todo graph and other details
    Intent intent;
    TextView textView1;
    TextView textView2;

    private Firebase mRef;
    private Firebase tableListRef;
    private Firebase currentTableRef;

    LineGraphSeries series;
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        intent = getIntent();
        final String TABLE_NAME = intent.getStringExtra("table");

        textView1 = findViewById(R.id.specified_table);
        textView1.setText(TABLE_NAME);

        textView2 = findViewById(R.id.tableTVnumber);

        //setting up datapoints
        series = new LineGraphSeries();


        //this plots the graph

        GraphView myGraph = findViewById(R.id.myGraph);

        myGraph.addSeries(series);

        mRef = new Firebase(getResources().getString(R.string.firebase_url)); //Getting data from firebase
        tableListRef = mRef.child(TABLE_NAME);
        //this formats the labels on the table
        myGraph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return sdf.format(new Date((long) value));
                } else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });
        makeGraphFromFireBase();

        //gets current number of people at the table
        currentTableRef = mRef.child("current").child(TABLE_NAME);
        currentTableRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    //note dataSnapshot.getValue returns a long
                    int number = Integer.valueOf(dataSnapshot.getValue()+"");
                    textView2.setText(number+"");
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
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

    public void makeGraphFromFireBase() {
        tableListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) //updating the textview in realtime

            {
                int index = 0;
                DataPoint[] dp = new DataPoint[(int) dataSnapshot.getChildrenCount()];
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Log.i("firebaseT", "key: " + ds.getKey()
                            + " value: " + ds.getValue());
                    PointValue pointValue = ds.getValue(PointValue.class);
                    dp[index] = new DataPoint(pointValue.getxValue(), pointValue.getyValue());
                    index++;
                }
                //reset data to new datapoint array
                series.resetData(dp);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

}
