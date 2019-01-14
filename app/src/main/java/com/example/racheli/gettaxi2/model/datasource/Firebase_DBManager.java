package com.example.racheli.gettaxi2.model.datasource;

import android.support.annotation.NonNull;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class Firebase_DBManager implements Backend{


    static final ArrayList<Driver> driverList = new ArrayList<Driver>();
    static final  ArrayList<Ride> rideList = new ArrayList<>();

    public static ArrayList<Ride> getRideList() {
        return rideList;
    }

    public static ArrayList<Driver> getDriverList() {
        return driverList;
    }

    static DatabaseReference driverRef;
    static DatabaseReference rideRef;
    static {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        driverRef = database.getReference("drivers");
        rideRef = database.getReference("rides");
    }

    @Override
    public void addDriver(final Driver driver, final Action<String> action) throws Exception
    {
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


    private static ChildEventListener driverRefChildEventListener;
    private static ChildEventListener rideRefChildEventListener;

   public static void notifyToDriverList() {

            driverRefChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Driver driver = dataSnapshot.getValue(Driver.class);
                    String id = dataSnapshot.getKey();
                   // driver.setId(Long.parseLong(id));
                    driverList.add(driver);


                    //notifyDataChange.OnDataChanged(driverList);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Driver driver = dataSnapshot.getValue(Driver.class);
                    Long id = Long.parseLong(dataSnapshot.getKey());
                    //Driver.setId(id);


                    for (int i = 0; i < driverList.size(); i++) {
                        if (driverList.get(i).getId().equals(id)) {
                            driverList.set(i, driver);
                            break;
                        }
                    }
                    //notifyDataChange.OnDataChanged(driverList);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Driver driver = dataSnapshot.getValue(Driver.class);
                    Long id = Long.parseLong(dataSnapshot.getKey());
                    //Driver.setId(id);

                    for (int i = 0; i < driverList.size(); i++) {
                        if (driverList.get(i).getId() == id.toString()) {
                            driverList.remove(i);
                            break;
                        }
                    }
                    //notifyDataChange.OnDataChanged(driverList);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //notifyDataChange.onFailure(databaseError.toException());
                }
            };
            driverRef.addChildEventListener(driverRefChildEventListener);
        }

   /*public static void notifyToRidesList() {

        rideRefChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Ride ride = dataSnapshot.getValue(Ride.class);
                String id = dataSnapshot.getKey();
                // driver.setId(Long.parseLong(id));
                rideList.add(ride);


                //notifyDataChange.OnDataChanged(driverList);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Ride ride = dataSnapshot.getValue(Ride.class);
                Long id = Long.parseLong(dataSnapshot.getKey());
                //Driver.setId(id);
                for (int i = 0; i < driverList.size(); i++) {
                        rideList.set(i, ride);
                        break;
                    }
                }
                //notifyDataChange.OnDataChanged(driverList);


            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Driver driver = dataSnapshot.getValue(Driver.class);
                Long id = Long.parseLong(dataSnapshot.getKey());
                //Driver.setId(id);

                for (int i = 0; i < driverList.size(); i++) {
                    if (driverList.get(i).getId() == id.toString()) {
                        driverList.remove(i);
                        break;
                    }
                }
                //notifyDataChange.OnDataChanged(driverList);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //notifyDataChange.onFailure(databaseError.toException());
            }
        };
        driverRef.addChildEventListener(driverRefChildEventListener);
    }


    public static void stopNotifyToStudentList() {
        if (driverRefChildEventListener != null) {
            driverRef.removeEventListener(driverRefChildEventListener);
            driverRefChildEventListener = null;

        }
    }*/
    public static void NotifyToRideList(final NotifyDataChange<List<Ride>> notifyDataChange) {
        if (notifyDataChange != null) {
            if (rideRefChildEventListener != null) {
                notifyDataChange.onFailure(new Exception("first unNotify rides list"));
                return;
            }
            rideList.clear();
            rideRefChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Ride ride = dataSnapshot.getValue(Ride.class);
                    String id = dataSnapshot.getKey();
                    //Ride.setId(Long.parseLong(id));
                    rideList.add(ride);
                    notifyDataChange.OnDataChanged(rideList);
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Ride ride = dataSnapshot.getValue(Ride.class);
                    //Long id = Long.parseLong(dataSnapshot.getKey());
                    //ride.setId(id);
                    String name = ride.getDriverName();
                    String time = ride.getStartingTime();

                    for (int i = 0; i < rideList.size(); i++) {
                      if (rideList.get(i).getStartingTime().equals(time) &&
                              rideList.get(i).getDriverName().equals(name)) {
                            rideList.set(i, ride);
                            break;
                       }
                    }
                    notifyDataChange.OnDataChanged(rideList);
                }
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Ride ride = dataSnapshot.getValue(Ride.class);
                    String name = ride.getDriverName();
                    String time = ride.getStartingTime();
                    for (int i = 0; i < rideList.size(); i++) {
                        if (rideList.get(i).getDriverName().equals(name) &&
                                rideList.get(i).getDriverName().equals(name)) {
                            rideList.remove(i);
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
    public static void stopNotifyToRideList() {
        if (rideRefChildEventListener != null) {
            rideRef.removeEventListener(rideRefChildEventListener);
            rideRefChildEventListener = null;

        }
    }
}
