package com.example.racheli.gettaxi2.model.datasource;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.racheli.gettaxi2.controller.MainActivity;
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



import com.google.firebase.database.Query;


import java.util.ArrayList;
import java.util.List;

public class Firebase_DBManager implements Backend {


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

   /* public Firebase_DBManager() {
        notifyToDriverList(new NotifyDataChange<List<Driver>>() {
            @Override
            public void OnDataChanged(List<Driver> obj) {

            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
        // notifyToDriverList();
    }*/

    public static List<Driver> getDriverList() {
        return driverList;
    }

    public static List<Ride> getRideList() {
        return rideList;
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

            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
        return rideList;
    }
   public List<Driver> getDrivers()
   {
       notifyToDriverList(new NotifyDataChange<List<Driver>>() {
           @Override
           public void OnDataChanged(List<Driver> obj) {

           }

           @Override
           public void onFailure(Exception exception) {

           }
       });
       return driverList;
   }

    /*public static void notifyToDriverList(final NotifyDataChange<List<Driver>> notifyDataChange) {
        if (notifyDataChange != null) {
            if (driverRefChildEventListener != null) {
                notifyDataChange.onFailure(new Exception("first unNotify student list"));
                return;
            }
            driverList.clear();
            rideRefChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Driver driver = dataSnapshot.getValue(Driver.class);
                    String id = dataSnapshot.getKey();
                    //  driver.setId((id));
                    driverList.add(driver);
                    notifyDataChange.OnDataChanged(driverList);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Driver driver = dataSnapshot.getValue(Driver.class);
                    //Long id = Long.parseLong(dataSnapshot.getKey());
                    //Driver.setId(id);
                    for (int i = 0; i < driverList.size(); i++) {
                        if (driverList.get(i).getId().equals(driver.getId())) {
                            driverList.set(i, driver);
                            break;
                        }
                    }
                    notifyDataChange.OnDataChanged(driverList);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Driver driver = dataSnapshot.getValue(Driver.class);
                    //Long id = Long.parseLong(dataSnapshot.getKey());
                    //student.setId(id.toString());
                    for (int i = 0; i < driverList.size(); i++) {
                        if (driverList.get(i).getId().equals(driver.getId())) {
                            driverList.remove(i);
                            break;
                        }
                    }
                    notifyDataChange.OnDataChanged(driverList);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    notifyDataChange.onFailure(databaseError.toException());
                }
            };
            driverRef.addChildEventListener(driverRefChildEventListener);//falls here
        }
    }*/


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
