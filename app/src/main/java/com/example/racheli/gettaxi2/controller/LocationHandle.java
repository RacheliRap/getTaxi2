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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationHandle extends Activity{
    double longitude = 0;
    double latitude;
    Location locationA;
    Location location;
    View view;

        private float calculateDistance(Location a, Location b) {

                float distance = a.distanceTo(b);
                if(distance > 1000)
                {
                        distance = distance/1000;
                }
                return distance;

        }

        private void getLocation() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
                } else {

                        // Android version is lesser than 6.0 or the permission is already granted.
                }

                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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

                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
                location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(location != null) {
                        longitude = location.getLongitude();
                        latitude = location.getLatitude();
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


        public String getPlace(Location location) {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (addresses.size() > 0) {
                                String cityName = addresses.get(0).getAddressLine(0);
                                String stateName = addresses.get(0).getAddressLine(1);
                                String countryName = addresses.get(0).getAddressLine(2);
                                return stateName + "\n" + cityName + "\n" + countryName;
                        }
                        return "no place: \n ("+location.getLongitude()+" , "+location.getLatitude()+")";
                }
                catch (IOException e)
                {
                        e.printStackTrace();
                }
                return "IOException ...";
        }



}

