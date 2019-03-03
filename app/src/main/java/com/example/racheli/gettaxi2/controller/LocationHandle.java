package com.example.racheli.gettaxi2.controller;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;


import java.util.List;

public class LocationHandle extends Activity{
    double longitude;
    double latitude;
    private Location destLocation;
    private Location originLocation;
    private Location myLocation;
    View view;
    Context context;



    public LocationHandle(Context context) {
        this.context = context;
    }

    public Location getDestLocation() {
        return destLocation;
    }

    public Location getMyLocation() {
        getLocation();
        return myLocation;
    }
    public Location getOriginLocation() {
        return originLocation;
    }

    public void setOriginLocation(Location originLocation) {
        this.originLocation = originLocation;
    }

    public void setDestLocation(Location destLocation) {
        this.destLocation = destLocation;
    }

    private void getLocation() {
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

                LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                if(lm != null) {
                    try {
                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                        myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (myLocation != null) {
                            longitude = myLocation.getLongitude();
                            latitude = myLocation.getLatitude();
                        }
                    }
                    catch (SecurityException se)
                    {

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

        public Location addressToLocation(String my_address)
        {
            Location tempLocation = null;
                try {
                        Geocoder gc = new Geocoder(context);
                        if (gc.isPresent()) {
                                List<Address> list = gc.getFromLocationName(my_address, 1);
                                Address address = list.get(0);
                                double lat = address.getLatitude();
                                double lng = address.getLongitude();
                                tempLocation = new Location("A");
                                tempLocation.setLatitude(lat);
                                tempLocation.setLongitude(lng);


                        }
                } catch (Exception e)
                {
                        Toast.makeText(view.getContext(), "can not convert to location", Toast.LENGTH_LONG).show();

                }
            return tempLocation;
        }

        public float calculateDistance(Location a, Location b) {

                float distance = a.distanceTo(b);
                if(distance > 1000)
                {
                        distance = distance/1000;
                }
                return distance;

        }

}

