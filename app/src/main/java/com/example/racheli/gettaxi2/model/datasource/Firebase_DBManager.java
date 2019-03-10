package com.example.racheli.gettaxi2.model.datasource;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.racheli.gettaxi2.controller.LocationClass;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Firebase_DBManager implements Backend {


    private static final String TAG = "Firebase_DBManager";
    static ArrayList<Driver> driverList = new ArrayList<>();
    static ArrayList<Ride> rideList = new ArrayList<>();
    private static ChildEventListener driverRefChildEventListener;
    private static ChildEventListener rideRefChildEventListener;
    static Context mContext;
    Boolean isComplete = false;

    static DatabaseReference driverRef;
    static DatabaseReference rideRef;

    static {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        driverRef = database.getReference("drivers");
        rideRef = database.getReference("rides");
    }

    public List<Driver> getDriverList() {
        return driverList;
    }

    public static List<Ride> getRideList() {
        return rideList;
    }

    /**
     * ctor
     * @param context app context
     */
    public Firebase_DBManager(Context context) {
        mContext = context;

        NotifyToDriverList(new NotifyDataChange<List<Driver>>() {
            @Override
            public void OnDataChanged(List<Driver> obj) {
                Log.d(TAG, "OnDataChanged() called with: obj = [" + obj.size() + "]");
                isComplete = true;

            }

            @Override
            public void onFailure(Exception exception) {
                Log.d(TAG, "onFailure() called with: exception = [" + exception + "]");

            }
        });

        NotifyToRideList(new NotifyDataChange<List<Ride>>() {
            @Override
            public void OnDataChanged(List<Ride> obj) {
                Log.d(TAG, "OnDataChanged() called with: obj = [" + obj.size() + "]");

            }

            @Override
            public void onFailure(Exception exception) {
                Log.d(TAG, "onFailure() called with: exception = [" + exception + "]");

            }
        });


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

    /**
     * @return ride list
     */
    public ArrayList<Ride> getRides() {
        return rideList;
    }

    /**
     * The function return all availble rides
     */
    @Override
    public ArrayList<Ride> getAvailableRides() {
        ArrayList<Ride> availableRides = new ArrayList<>();
        for (Ride r : rideList) {
            if (r.getStatus().equals("AVAILABLE")) {
                availableRides.add(r);
            }
        }
        return availableRides;
    }

    /**
     * The function return all rides by driver name
     */
    @Override
    public ArrayList<String> getDriversNames() {
        ArrayList<String> driversNames = new ArrayList<>();
        for (Driver d : driverList) {
            driversNames.add(d.getFullName());
        }
        return driversNames;
    }

    /**
     * The function return all the rides that were not handled
     */
    @Override
    public ArrayList<Ride> getUnhandledRides() {
        ArrayList<Ride> availableRides = new ArrayList<>();
        for (Ride r : rideList) {
            if (r.getStatus().equals("AVAILABLE")) {
                availableRides.add(r);
            }
        }
        return availableRides;
    }

    /**
     *The function return all the finished rides
     */
    @Override
    public ArrayList<Ride> getFinishedRides() {
        ArrayList<Ride> finishedRides = new ArrayList<>();
        for (Ride r : rideList) {
            if (r.getStatus().equals("DONE")) {
                finishedRides.add(r);
            }
        }
        return finishedRides;
    }

    /***
     * The function return all the rides of a specific friver
     * @param driverName
     * @return list with rides
     */
    @Override
    public ArrayList<Ride> getRidesByDriver(String driverName) {
        ArrayList<Ride> ridesByName = new ArrayList<>();
       for(Ride r:rideList)
       {
           if(r.getDriverName() == driverName)
           {
               ridesByName.add(r);
           }
       }
       return ridesByName;
    }

    /**
     * the function return all the rides to a specific fity
     * @param city
     * @return ride list
     */
    @Override
    public ArrayList<Ride> getRidesByCity(String city) {
        ArrayList<Ride> ridesByCity = new ArrayList<>();
        Geocoder gc = new Geocoder(mContext);
        try
        {
        if (gc.isPresent()) {
            for(Ride r: rideList) {
                //try to convert string of address to location
                List<Address> addresses = gc.getFromLocationName(r.getDestination(), 1);
                String cityName = addresses.get(0).getAddressLine(0);
                if(city.equals(cityName) && r.getStatus().equals("AVAILABLE"))
                {
                    ridesByCity.add(r);
                }
            } }
        }
        catch (Exception e)
        {
            Toast.makeText(mContext, "error", Toast.LENGTH_SHORT).show();
        }
        return ridesByCity;
    }

    /**
     * return all the rides that equal or smaller from a given distance
     * @param distance
     * @return ride of lists
     */
    @Override
    public ArrayList<Ride> getRidesByDistance(float distance) {
        LocationClass lc = new LocationClass(mContext);
        ArrayList<Ride> ridesByDistance = new ArrayList<>();
        float mDistance;
        //driver location
        Location driverLocation = lc.getMyLocation();
        for(Ride r : rideList) {
            Location distLocation = lc.addressToLocation(r.getDestination());
              mDistance = lc.calculateDistance(driverLocation, distLocation);
              if(mDistance <= distance)
              {
                  ridesByDistance.add(r);
              }
        }
        return ridesByDistance;
    }

    /**
     * return all rides that were on a specific date
     * @param date
     * @return
     */
    @Override
    public ArrayList<Ride> getRidesByDate(Date date) {
        ArrayList<Ride> ridesByDate = new ArrayList<>();
        for(Ride r : rideList)
        {
            if(r.getRideDate().equals(date.toString()))
            {
                ridesByDate.add(r);
            }
        }
        return null;
    }

    /**
     * calculate payment for all rides and return the ones that under a threshold payment
     * @param payment
     * @return
     */
    @Override
    public ArrayList<Ride> getRidesByPayment(float payment) {
        ArrayList<Ride> ridesByPayment = new ArrayList<>();
        String a,b;
        Location dest, origin;
        LocationClass lc = new LocationClass(mContext);
        float distance, mPayment;
        for(Ride r : rideList)
        {
            //calculate only for dine rides
            if(r.getStatus().equals("DONE"))
            {
                a = r.getDestination();
                b = r.getOrigin();
                dest = lc.addressToLocation(a);
                origin = lc.addressToLocation(b);
                distance = lc.calculateDistance(dest , origin);
                mPayment = distance*10;
                if(mPayment <= payment)
                {
                    ridesByPayment.add(r);
                }
            }
        }
        return ridesByPayment;
    }

    /**
     * the function update details in rides
     * @param id of the entry to update
     * @param key which field in the entry
     * @param value the value to update
     */
    @Override
    public void updateRide(String id, String key, String value) {
        try {
            rideRef.child(id).child(key).setValue(value);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean isComplete() {
        return isComplete;
    }

    /**
     * @return all drivers list
     */
    public ArrayList<Driver> getDrivers() {

        return driverList;
    }

    /**
     * stop the listener fron notifying to Firebase
     */
    public static void stopNotifyToRideList() {
        if (rideRefChildEventListener != null) {
            rideRef.removeEventListener(rideRefChildEventListener);
            rideRefChildEventListener = null;
        }
    }
    /**
     * stop the listener fron notifying to Firebase
     */
    public static void stopNotifyToDriverList() {
        if (driverRefChildEventListener != null) {
            driverRef.removeEventListener(driverRefChildEventListener);
            driverRefChildEventListener = null;
        }
    }


    /**
     *    Attach a listener to read the data at our Drivers reference
     */
    public static void NotifyToDriverList(final NotifyDataChange<List<Driver>> notifyDataChange) {
        if (notifyDataChange != null) {
            if (driverRefChildEventListener != null) {
                notifyDataChange.onFailure(new Exception("first unNotify student list"));
                return;
            }
            driverList.clear();
            driverRefChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Driver driver = dataSnapshot.getValue(Driver.class);
                    String id = dataSnapshot.getKey();
                    driver.setId(id);
                    driverList.add(driver);
                    notifyDataChange.OnDataChanged(driverList);
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Driver driver = dataSnapshot.getValue(Driver.class);
                    String id = dataSnapshot.getKey();
                    for (int i = 0; i < driverList.size(); i++) {
                        if (driverList.get(i).getId().equals(id)) {
                            driverList.set(i, driver);
                            break;
                        }
                    }
                    notifyDataChange.OnDataChanged(driverList);
                }
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Driver driver = dataSnapshot.getValue(Driver.class);
                    String id = dataSnapshot.getKey();
                    for (int i = 0; i < driverList.size(); i++) {
                        if (driverList.get(i).getId().equals(id)) {
                            driverList.remove(i);
                            break;
                        }
                    }
                    notifyDataChange.OnDataChanged(driverList);
                }
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) { }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    notifyDataChange.onFailure(databaseError.toException());
                }
            };
           driverRef.addChildEventListener(driverRefChildEventListener);
        }
    }

    public static void NotifyToRideList(final NotifyDataChange<List<Ride>> notifyDataChange) {
        if (notifyDataChange != null) {
            if (rideRefChildEventListener != null) {
                notifyDataChange.onFailure(new Exception("first unNotify student list"));
                return;
            }
            rideList.clear();
            rideRefChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Ride ride = dataSnapshot.getValue(Ride.class);
                    String id = dataSnapshot.getKey();
                    ride.setID(id);
                    rideList.add(ride);
                    notifyDataChange.OnDataChanged(rideList);
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Ride ride = dataSnapshot.getValue(Ride.class);
                    String id = dataSnapshot.getKey();
                    ride.setID(id);
                   for (int i = 0; i < rideList.size(); i++) {
                        if (rideList.get(i).getID().equals(id)) {
                            rideList.set(i, ride);
                            break;
                        }
                    }
                    notifyDataChange.OnDataChanged(rideList);
                }
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Ride ride = dataSnapshot.getValue(Ride.class);
                    String id = dataSnapshot.getKey();
                    ride.setID(id);
                    for (int i = 0; i < rideList.size(); i++) {
                        if (rideList.get(i).getID().equals(id)) {
                            rideList.set(i, ride);
                            break;
                        }
                    }
                    notifyDataChange.OnDataChanged(rideList);
                }
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) { }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    notifyDataChange.onFailure(databaseError.toException());
                }
            };
            rideRef.addChildEventListener(rideRefChildEventListener);
        }
    }

    /**
     * The function add ride tofirebase
     * @param ride
     * @param action interface
     * @throws Exception
     */
    public void addRide(final Ride ride, final Action<String> action) throws Exception
    {
        rideRef.push().setValue(ride).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                action.onSuccess(" insert ride");
                action.onProgress("upload ride data", 100);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                action.onFailure(e);
                action.onProgress("error upload ride data", 100);

            }
        });
    }

}


