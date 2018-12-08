package com.example.garyo.smartcanteen;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.Arrays;

public class SmartNotification extends Service {
    public final String CHANNEL_ID = "Smart_Notification_Channel";
    public static final String TAG = "SmartService";
    Firebase currentRef;
    NotificationManager manager;
    Notification notification;


    public SmartNotification() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final int limit = intent.getIntExtra("limit",10);
        Log.i(TAG,"limit is "+ limit);
        currentRef = new Firebase(getResources().getString(R.string.firebase_url)).child("current");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG,"run");
                NotificationManager notificationManager = (NotificationManager) SmartNotification.this.getSystemService(Context.NOTIFICATION_SERVICE);

                //need this to run on android oreo and above
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);
                    // Configure the notification channel.
                    notificationChannel.setDescription("Channel description");
                    notificationChannel.enableLights(true);
                    notificationChannel.setLightColor(R.color.background);
                    notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                    notificationChannel.enableVibration(true);
                    notificationManager.createNotificationChannel(notificationChannel);
                }
                //building notification
                NotificationCompat.Builder builder = new NotificationCompat.Builder(SmartNotification.this,CHANNEL_ID)
                        .setContentTitle("Smart Canteen")
                        .setContentText("Canteen has less than "+ limit +" people")
                        .setAutoCancel(true)
                        .setPriority(Notification.PRIORITY_DEFAULT)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setSmallIcon(R.drawable.white_leaf);
                notification = builder.build();
                 manager = (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);
                //change when to notify base on firebase;
                currentRef.addValueEventListener(new ValueEventListener() {
                    int totalNumberOfPeople;
                    ArrayList<DataPoint> dataPointArrayList = new ArrayList<>();
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        totalNumberOfPeople = 0;
                        View minView = null;
                        int min = 9999999;
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            Log.i("firebaseM", "key: " + ds.getKey()
                                    + " value: " + ds.getValue());
                                int x = Integer.parseInt(ds.getValue().toString());

                                //get total
                                totalNumberOfPeople+= x;
                        }
                        if(totalNumberOfPeople<=limit){
                            SmartNotification.this. manager.notify(1234, notification);
                        }
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });

                // broadcast a message back to the app to inform it that we are done
                Intent done = new Intent();
                sendBroadcast(done);
            }
        }
        );
        thread.start();
        return START_NOT_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"service destroyed");
    }
}
