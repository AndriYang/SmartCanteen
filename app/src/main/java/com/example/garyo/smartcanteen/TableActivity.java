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
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class TableActivity extends AppCompatActivity {
    //todo graph and other details
    Intent intent;
    TextView textView1;
    private TextView mValueView;
    private Firebase mRef;
    LineGraphSeries series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        intent = getIntent();
        final String TABLE_NAME = intent.getStringExtra("table");

        textView1 = findViewById(R.id.specified_table);
        textView1.setText(TABLE_NAME);

        series = new LineGraphSeries();

        GraphView myGraph = findViewById(R.id.myGraph);
        myGraph.addSeries(series);
        mValueView = (TextView) findViewById(R.id.tableTextView);
        mRef = new Firebase("https://smart-canteen-45be9.firebaseio.com/" + TABLE_NAME); //Getting data from firebase

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) //updating the textview in realtime

            {
                int index = 0;
                DataPoint[] dp = new DataPoint[(int) dataSnapshot.getChildrenCount()];
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Log.i("TAG", "key: " + ds.getKey()
                            + " value: " + ds.getValue());
                    PointValue pointValue = ds.getValue(PointValue.class);
                    //create TextView widget
                    dp[index] = new DataPoint(pointValue.getxValue(), pointValue.getyValue());
                    index++;
                }
                series.resetData(dp);

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


}
