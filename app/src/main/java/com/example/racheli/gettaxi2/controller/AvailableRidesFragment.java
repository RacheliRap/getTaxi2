package com.example.racheli.gettaxi2.controller;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.racheli.gettaxi2.R;
import com.example.racheli.gettaxi2.model.backend.Backend;
import com.example.racheli.gettaxi2.model.backend.BackendFactory;
import com.example.racheli.gettaxi2.model.datasource.Firebase_DBManager;
import com.example.racheli.gettaxi2.model.entities.Driver;
import com.example.racheli.gettaxi2.model.entities.Ride;

import java.util.ArrayList;
import java.util.List;

//import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

public class AvailableRidesFragment extends Activity
{
    RecyclerView recyclerView;
    ArrayList<Ride> rideList = new ArrayList<Ride>() {};
    double longitude = 0;
    double latitude;
    Location locationA;
    Location location;
    View view;
    Context context;



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

   @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.available_rides_layout);
        context = this;
        recyclerView = (RecyclerView) findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ExpendableAdapter(initDemoItems()));
        //getLocation();
    }


    private void getLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
            return; // has no permission it will crash if we will try to access it
        }

        final LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                if(location != null) {
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(lm != null) {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 5){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLocation();
            }
        }
    }

    public void addressToLocation(String my_address)
    {
        try {
            Geocoder gc = new Geocoder(this);
            if (gc.isPresent()) {
                List<Address> list = gc.getFromLocationName(my_address, 1);
                Address address = list.get(0);
                double lat = address.getLatitude();
                double lng = address.getLongitude();
                locationA = new Location("A");
                locationA.setLatitude(lat);
                locationA.setLongitude(lng);
            }
        } catch (Exception e)
        {
            Toast.makeText(view.getContext(), "can not convert to location", Toast.LENGTH_LONG).show();
        }
    }

    private float calculateDistance(Location a, Location b) {

        float distance = a.distanceTo(b);
        if(distance > 1000)
        {
            distance = distance/1000;
        }
        return distance;

    }

    private List<Ride> initDemoItems() {
       List<Ride> result = new ArrayList<>(1000);
       Backend fb  = BackendFactory.getInstance();
       List<Driver> driverList = Firebase_DBManager.getDriverList();
       List<Ride> rlist = Firebase_DBManager.getRideList();
        for(int i=0;i<3;i++)
       {
           Ride ride = new Ride();
           ride.setCreditCard(i+" 111222233334444");
           ride.setDestination(i+" avraham shiff, jerusalem");
           ride.setDriverName(i+" choen");
           ride.setOrigin(i + "mhala mall");
           ride.setPassengerMail(i+" ride@gmail.com");
           ride.setPhoneNumber(i+"77777777");
           rideList.add(ride);
       }

       for (int i=0;i<3;i++) {
           ExpendableItem item = new ExpendableItem();
           item.setDestination(rideList.get(i).getDestination().toString());
            //getLocation();
           //addressToLocation(rideList.get(i).getDestination().toString());
           //float distance =  calculateDistance(locationA, location);
           //item.setDistance(distance);
           //item.time = rideList.get(i).getStartingTime();
           ChildItem child = new ChildItem();
           //child.location = "Hi, im: "+ i +" and I'm a child of " + i;
           child.setDestination( rideList.get(i).getOrigin().toString());
           item.childs.add(child);
           result.add(item);
       }
       return result;
   }

    private static class ExpendableAdapter extends RecyclerView.Adapter {
        List<Ride> data;
        public ExpendableAdapter(List<Ride> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if(viewType == TYPES.EXPENDABLE.value){
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expendable,parent,false);
                return new ExpendableViewHolder(view);
            }else{
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child,parent,false);
                return new ChildViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Ride item = data.get(position);
            if (item instanceof ExpendableItem){
                ExpendableViewHolder exHolder = (ExpendableViewHolder) holder;
                exHolder.location_txt.setText(item.getDestination());
               // exHolder.distance_txt.setText((ExpendableItem) item.getDistance());
            }else {
                ChildViewHolder chHolder = (ChildViewHolder) holder;
                chHolder.txt.setText(item.getDestination());
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        enum TYPES{
            EXPENDABLE(1),CHILD(2);

            private final int value;
            TYPES(int i) {
                value = i;
            }
        }
        @Override
        public int getItemViewType(int position) {
            return data.get(position) instanceof ExpendableItem ? TYPES.EXPENDABLE.value : TYPES.CHILD.value;
        }


        class ChildViewHolder extends RecyclerView.ViewHolder{
            TextView txt;
            public ChildViewHolder(View itemView) {
                super(itemView);
                txt = itemView.findViewById(R.id.dest_textView);
            }
        }


        class ExpendableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView location_txt;
            TextView distance_txt;
            ImageView btn;
            ImageButton arrow;
            ImageButton plus;
            Context context;


            public ExpendableViewHolder(View itemView) {
                super(itemView);
                location_txt = itemView.findViewById(R.id.parent_location);
                distance_txt = itemView.findViewById(R.id.parent_dist);
                //btn = itemView.findViewById(R.id.expendable_btn);
               // btn.setOnClickListener(this);
                arrow = itemView.findViewById(R.id.expendable_btn);
                arrow.setOnClickListener(this);
                plus = itemView.findViewById(R.id.getRide_btn);
                plus.setOnClickListener(this);

            }

            @Override
            public void onClick(View v) {
                //the button for expend the list view
                if( v == arrow) {
                    ExpendableItem item = ((ExpendableItem) data.get(getAdapterPosition()));
                    int numOfChildes = item.childs.size();
                    if (item.isOpen) {
                        data.removeAll(item.childs);
                        notifyItemRangeRemoved(getAdapterPosition() + 1, numOfChildes);
                        item.isOpen = false;
                    } else {
                        data.addAll(getAdapterPosition() + 1, item.childs);
                        notifyItemRangeInserted(getAdapterPosition() + 1, numOfChildes);
                        item.isOpen = true;
                    }
                }
                //the button to get the ride
                if(v == plus) {
                       // Toast.makeText(context, "hi", Toast.LENGTH_LONG).show();
                        /*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                        alertDialogBuilder.setTitle("dialog title");
                        alertDialogBuilder.setMessage("dialog message ....");
                        alertDialogBuilder.setPositiveButton("Ok",onClickListener);
                        alertDialogBuilder.setNegativeButton("Cancel ",onClickListener);
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();*/
                    }

            }
            AlertDialog.OnClickListener onClickListener = new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch(which)
                    {
                        case Dialog.BUTTON_NEGATIVE:
                        {

                        }
                    }

                } };
        }
    }

    private class ExpendableItem extends Ride {
        public boolean isOpen;
        Float distance;
        List<ChildItem> childs = new ArrayList<>();

        public void setDistance(Float distance) {
            this.distance = distance;
        }

        public Float getDistance() {
            return distance;
        }
    }
    private class ChildItem extends Ride  {

    }


}
