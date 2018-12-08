package com.example.garyo.smartcanteen;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Canteen extends AppCompatActivity {
    Firebase mRef,currentRef;
    int totalNumberOfPeople;
    SharedPreferences prefs;
    GraphView graphView;
    int capacity;
    LineGraphSeries series = new LineGraphSeries();
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen);
        graphView = findViewById(R.id.canteenGraph);
        graphView.addSeries(series);

        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return sdf.format(new Date((long) value));
                } else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });


        //allows user to set canteen capacity default to 100
        prefs = getSharedPreferences("filename",MODE_PRIVATE);
        capacity = prefs.getInt(Settings.CAPACITY_KEY,100);

        mRef = new Firebase(getResources().getString(R.string.firebase_url));
        currentRef = mRef.child("current");

        final TextView canteenNoOfPpl = findViewById(R.id.canteenNoOfPpl);
        //gets total number of people and updates circle graph
        currentRef.addValueEventListener(new ValueEventListener() {
            int index=0;
            ArrayList<DataPoint> dataPointArrayList = new ArrayList<>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                totalNumberOfPeople = 0;
                View minView = null;
                int min = 9999999;
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Log.i("firebaseM", "key: " + ds.getKey()
                            + " value: " + ds.getValue());
                    try{
                        int x = Integer.parseInt(ds.getValue().toString());

                        //get total
                        totalNumberOfPeople+= x;

                    }catch (NumberFormatException e){
                        e.printStackTrace();
                    }catch(ArithmeticException e){
                        e.printStackTrace();
                        capacity = 100;
                    }
                }
                canteenNoOfPpl.setText(totalNumberOfPeople+"");
                dataPointArrayList.add(new DataPoint(System.currentTimeMillis(),totalNumberOfPeople));
                DataPoint[] dp =   dataPointArrayList.toArray(new DataPoint[dataPointArrayList.size()]);
                Log.i("dptest",Arrays.deepToString(dp));
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
