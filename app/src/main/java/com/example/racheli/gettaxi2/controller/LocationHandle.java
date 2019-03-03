package com.example.racheli.gettaxi2.controller;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationHandle extends Activity{
    double longitude;
    double latitude;
    private Location locationA;
    private Location myLocation;
    View view;
    Context context;

    public LocationHandle(Context context) {
        this.context = context;
    }

    public Location getLocationA() {
        return locationA;
    }

    public Location getMyLocation() {
        getLocation();
        return myLocation;
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

        public void addressToLocation(String my_address)
        {
                try {
                        Geocoder gc = new Geocoder(context);
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

        public float calculateDistance(Location a, Location b) {

                float distance = a.distanceTo(b);
                if(distance > 1000)
                {
                        distance = distance/1000;
                }
                return distance;

        }

}

