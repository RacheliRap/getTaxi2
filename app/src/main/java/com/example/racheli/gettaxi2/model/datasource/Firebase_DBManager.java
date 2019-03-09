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
    static List<Driver> driverList = new ArrayList<>();
    static List<Ride> rideList = new ArrayList<>();
    private static ChildEventListener driverRefChildEventListener;
    private static ChildEventListener rideRefChildEventListener;
    public List<Driver> tmp = new ArrayList<>();
    static Context mContext;

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

    public Firebase_DBManager(Context context) {
        mContext = context;

        NotifyToDriverList(new NotifyDataChange<List<Driver>>() {
            @Override
            public void OnDataChanged(List<Driver> obj) {
                Log.d(TAG, "OnDataChanged() called with: obj = [" + obj.size() + "]");

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

    public List<Ride> getRides() {
        /*rideRef.orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Ride ride = snapshot.getValue(Ride.class);
                    rideList.add(ride);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        return rideList;
    }

    @Override
    public ArrayList<Ride> getAvailableRides() {
        ArrayList<Ride> availableRides = new ArrayList<>();
        for (Ride r : rideList) {
            if (r.getStatus() == "AVAILABLE") {
                availableRides.add(r);
            }
        }
        return availableRides;
    }

    @Override
    public ArrayList<String> getDriversNames() {
        ArrayList<String> driversNames = new ArrayList<>();
        for (Driver d : driverList) {
            driversNames.add(d.getFullName());
        }
        return driversNames;
    }

    @Override
    public ArrayList<Ride> getUnhandledRides() {
        ArrayList<Ride> availableRides = new ArrayList<>();
        for (Ride r : rideList) {
            if (r.getStatus() == "AVAILABLE") {
                availableRides.add(r);
            }
        }
        return availableRides;
    }

    @Override
    public ArrayList<Ride> getFinishedRides() {
        ArrayList<Ride> finishedRides = new ArrayList<>();
        for (Ride r : rideList) {
            if (r.getStatus() == "DONE") {
                finishedRides.add(r);
            }
        }
        return finishedRides;
    }

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

    @Override
    public ArrayList<Ride> getRidesByCity(String city) {
        ArrayList<Ride> ridesByCity = new ArrayList<>();
        Geocoder gc = new Geocoder(mContext);
        try
        {
        if (gc.isPresent()) {
            for(Ride r: rideList) {
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

    @Override
    public ArrayList<Ride> getRidesByDistance(float distance) {
        LocationClass lc = new LocationClass(mContext);
        ArrayList<Ride> ridesByDistance = new ArrayList<>();
        float mDistance;
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

    @Override
    public ArrayList<Ride> getRidesByPayment(float payment) {
        ArrayList<Ride> ridesByPayment = new ArrayList<>();
        String a,b;
        Location dest, origin;
        LocationClass lc = new LocationClass(mContext);
        float distance, mPayment;
        for(Ride r : rideList)
        {
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

    @Override
    public void updateRide(String id, String key, String value) {
        try {
            rideRef.child(id).child(key).setValue(value);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void tmp(List<Driver> a)
    {
        tmp.addAll(a);
    }
    public void getDrivers(final MyCallback myCallback) {

        /*driverRef.orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Driver driver = snapshot.getValue(Driver.class);
                    //driverList.add(driver);
                    myCallback.onCallback(driver);
                   // tmp(driverList);
                }
                done(driverList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        // return driverList;*/
    }

    public void callGetDrivers()
    {
        getDrivers(new MyCallback() {
            @Override
            public void onCallback(Driver value) {
                driverList.add(value);
            }
        });
    }
    private void done(List<Driver> ls) {
        tmp.addAll(ls);
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
                    Driver student = dataSnapshot.getValue(Driver.class);
                    //Long id = Long.parseLong(dataSnapshot.getKey());
                    /*for (int i = 0; i < studentList.size(); i++) {
                        if (studentList.get(i).getId().equals(id)) {
                            studentList.set(i, student);
                            break;
                        }
                    }*/
                    notifyDataChange.OnDataChanged(driverList);
                }
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Driver driver = dataSnapshot.getValue(Driver.class);
                    Long id = Long.parseLong(dataSnapshot.getKey());
                    /*for (int i = 0; i < studentList.size(); i++) {
                        if (studentList.get(i).getId() ==  id) {
                            studentList.remove(i);
                            break;
                        }
                    }*/
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
                    //R.setId(Long.parseLong(id));
                    rideList.add(ride);
                    notifyDataChange.OnDataChanged(rideList);
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Ride ride = dataSnapshot.getValue(Ride.class);
                    String id = dataSnapshot.getKey();
                    ride.setID(id);
                   /* for (int i = 0; i < studentList.size(); i++) {
                        if (studentList.get(i).getId().equals(id)) {
                            studentList.set(i, student);
                            break;
                        }
                    }    */
                    notifyDataChange.OnDataChanged(rideList);
                }
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Ride ride = dataSnapshot.getValue(Ride.class);
                    Long id = Long.parseLong(dataSnapshot.getKey());
                    /*for (int i = 0; i < studentList.size(); i++) {
                        if (studentList.get(i).getId() ==  id) {
                            studentList.remove(i);
                            break;
                        }
                    }      */
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


