package com.example.racheli.gettaxi2.controller;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.content.Context;


import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class Destination{
        public LatLng getLocationFromAddress(Context context, String strAddress) {
            //Java.Object activity = Android.ActivityUtils.GetRootActivity();
            //locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

                Geocoder coder = new Geocoder(context);
                List<Address> address;
                LatLng p1 = null;

                try {
                        // May throw an IOException
                        address = coder.getFromLocationName(strAddress, 5);
                        if (address == null) {
                                return null;
                        }

                        Address location = address.get(0);
                        p1 = new LatLng(location.getLatitude(), location.getLongitude() );

                } catch (IOException ex) {

                        ex.printStackTrace();
                }

                return p1;
        }
}

