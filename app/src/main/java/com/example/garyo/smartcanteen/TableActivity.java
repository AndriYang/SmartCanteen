package com.example.garyo.smartcanteen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class TableActivity extends AppCompatActivity {
    //todo graph and other details
    Intent intent;
    TextView textView1;
    private TextView mValueView;
    private Firebase mRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        intent = getIntent();
        String s = intent.getStringExtra("table");
        textView1 = findViewById(R.id.specified_table);
        textView1.setText(s);
        mValueView = (TextView) findViewById(R.id.tableView);
        mRef = new Firebase("https://sutd-smart-canteen.firebaseio.com/Table1"); //Getting data from firebase
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) //updating the textview in realtime

            {

                String value = dataSnapshot.getValue(String.class);

                mValueView.setText(value); //replace the textview with data from firebase
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
