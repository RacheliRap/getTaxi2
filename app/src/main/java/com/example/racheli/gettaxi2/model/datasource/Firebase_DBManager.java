package com.example.racheli.gettaxi2.model.datasource;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.racheli.gettaxi2.model.backend.Backend;
import com.example.racheli.gettaxi2.model.entities.Driver;
import com.example.racheli.gettaxi2.model.entities.Ride;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Firebase_DBManager implements Backend {


    private static final String TAG = "Firebase_DBManager";
    static List<Driver> driverList = new ArrayList<Driver>();
    static List<Ride> rideList = new ArrayList<Ride>();
    private static ChildEventListener driverRefChildEventListener;
    private static ChildEventListener rideRefChildEventListener;

    static DatabaseReference driverRef;
    static DatabaseReference rideRef;

    static {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        driverRef = database.getReference("drivers");
        rideRef = database.getReference("rides");
    }


    public static List<Driver> getDriverList() {
        return driverList;
    }

    public static List<Ride> getRideList() {
        return rideList;
    }

    public Firebase_DBManager()
    {
        getDrivers();
        getRides();
    }

    @Override
    public void addDriver(final Driver driver, final Action<String> action) throws Exception {
        driverRef.push().setValue(driver).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                action.onSuccess(" insert Driver");
                action.onProgress("upload driver data", 100);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                action.onFailure(e);
                action.onProgress("error upload driver data", 100);

            }
        });
    }

    public List<Ride> getRides()
    {
       notifyToRideList(new NotifyDataChange<List<Ride>>() {
            @Override
            public void OnDataChanged(List<Ride> obj) {
                Log.d(TAG, "OnDataChanged() called with: obj = [" + obj.size() + "]");

            }

            @Override
            public void onFailure(Exception exception) {
                Log.d(TAG, "onFailure() called with: exception = [" + exception + "]");

            }
        });
        return rideList;
    }

    @Override
    public ArrayList<Ride> getAvailableRides() {
        ArrayList<Ride> availableRides = new ArrayList<>();
        for(Ride r : rideList)
        {
            if(r.getStatus() == "AVAILABLE")
            {
                availableRides.add(r);
            }
        }
        return availableRides;
    }

    @Override
    public ArrayList<String> getDriversNames() {
        ArrayList<String> driversNames = new ArrayList<>();
        for(Driver d: driverList)
        {
            driversNames.add(d.getFullName());
        }
        return driversNames;
    }

    @Override
    public ArrayList<Ride> getUnhandledRides() {
        ArrayList<Ride> availableRides = new ArrayList<>();
        for(Ride r : rideList)
        {
            if(r.getStatus() == "AVAILABLE")
            {
                availableRides.add(r);
            }
        }
        return availableRides;
    }

    @Override
    public ArrayList<Ride> getFinishedRides() {
        ArrayList<Ride> finishedRides = new ArrayList<>();
        for(Ride r : rideList)
        {
            if(r.getStatus() == "DONE")
            {
                finishedRides.add(r);
            }
        }
        return finishedRides;
    }

    @Override
    public ArrayList<Ride> getRidesByDriver(String driverName) {
        return null;
    }

    @Override
    public ArrayList<Ride> getRidesByCity(String city) {
        return null;
    }

    @Override
    public ArrayList<Ride> getRidesByDistance(float distance) {
        return null;
    }

    @Override
    public ArrayList<Ride> getRidesByDate(Date date) {
        return null;
    }

    @Override
    public ArrayList<Ride> getRidesByPayment(float payment) {
        return null;
    }

    public List<Driver> getDrivers()
   {
       notifyToDriverList(new NotifyDataChange<List<Driver>>() {
           @Override
           public void OnDataChanged(List<Driver> obj) {
               Log.d(TAG, "OnDataChanged() called with: obj = [" + obj.size() + "]");

           }

           @Override
           public void onFailure(Exception exception) {
               Log.d(TAG, "onFailure() called with: exception = [" + exception + "]");

           }
       });
       return driverList;
   }

    public static void stopNotifyToRideList() {
        if (rideRefChildEventListener != null) {
            rideRef.removeEventListener(rideRefChildEventListener);
            rideRefChildEventListener = null;


        }
    }
    public static void stopNotifyToDriverList() {
        if (driverRefChildEventListener != null) {
            driverRef.removeEventListener(driverRefChildEventListener);
            driverRefChildEventListener = null;
        }
    }


    // Attach a listener to read the data at our Drivers reference
    public void notifyToDriverList(final NotifyDataChange<List<Driver>> notifyDataChange) {
        if (notifyDataChange != null) {
            if (driverRefChildEventListener != null) {
                notifyDataChange.onFailure(new Exception("first unNotify driver list"));
                return;
            }
            driverList.clear();
            driverRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                    Driver driver = dataSnapshot.getValue(Driver.class);
                    driverList.add(driver);
                    notifyDataChange.OnDataChanged(driverList);
                   // System.out.println("Previous Post ID: " + prevChildKey);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }
    public void notifyToRideList(final NotifyDataChange<List<Ride>> notifyDataChange) {
        if (notifyDataChange != null) {
            if (rideRefChildEventListener != null) {
                notifyDataChange.onFailure(new Exception("first unNotify ride list"));
                return;
            }
            rideList.clear();
            rideRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                    Ride ride = dataSnapshot.getValue(Ride.class);
                    rideList.add(ride);
                    notifyDataChange.OnDataChanged(rideList);
                    //System.out.println("Previous Post ID: " + prevChildKey);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }
}
