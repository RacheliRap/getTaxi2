package com.example.racheli.gettaxi2.model.datasource;

import android.support.annotation.NonNull;
import com.example.racheli.gettaxi2.model.backend.Backend;
import com.example.racheli.gettaxi2.model.entities.Driver;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Firebase_DBManager implements Backend{

    final ArrayList<Driver> driverList = new ArrayList<Driver>();

    static DatabaseReference driverRef;
    static {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        driverRef = database.getReference("drivers");
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

    public void retrieveData()
    {
        driverRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot driverSnapshot: dataSnapshot.getChildren()) {
                    driverList.add(driverSnapshot.getValue(Driver.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
