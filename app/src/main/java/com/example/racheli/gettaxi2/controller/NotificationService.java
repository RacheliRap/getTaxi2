package com.example.racheli.gettaxi2.controller;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.example.racheli.gettaxi2.model.datasource.Firebase_DBManager;
import com.example.racheli.gettaxi2.model.backend.BackendFactory;
import com.example.racheli.gettaxi2.model.entities.Ride;

import java.util.List;

/***
 * The class represent the service that is activated when the data in the ride list changes and child is added
 * intent that runs in the background not deepened in activity
 * it sends a flag to the broadcastReceiver every time a new ride is added
 */
public class NotificationService extends Service {

    private int lastCount = 0;
    Context context;
    Firebase_DBManager dbManager;

    /***
     * this class shows what happens every time something is requested from the service
     */
    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {//when we turn up the service
        dbManager = (Firebase_DBManager) BackendFactory.getInstance(getApplicationContext());//set dbmanager
        context = getApplicationContext();//gets context
        dbManager.NotifyToRideList(new Firebase_DBManager.NotifyDataChange<List<Ride>>() {//open listing to drive list
            @Override
            public void onDataChanged(List<Ride> obj) {//adds ride in list
                try {
                    Intent intent = new Intent(context, BroadCastReceiverNotification.class);//create new intent
                    sendBroadcast(intent);//sends the notification to the broadcast
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Exception exception) {
            }
        });;
        return START_REDELIVER_INTENT;//says to system to continue the service even when the application closes.
    }

    /***
     * the system calls this method to retrieve the IBinder only when the first client connects
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
