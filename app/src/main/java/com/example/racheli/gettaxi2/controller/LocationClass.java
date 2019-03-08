package com.example.racheli.gettaxi2.controller;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * The class handles all thr function related to location
 */
public class LocationClass extends Activity{
    double longitude;
    double latitude;
    private Location destLocation;
    private Location originLocation;
    private Location myLocation;
    View view;
    static Context context;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;




    public LocationClass(Context context) {
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

    /**
     * The function sighed to location listener, ask for permission for location,
     * and try to get the phone's location.
     */
    private void getLocation() {
        //location listener
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

                android.location.LocationManager lm = (android.location.LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                if(lm != null) {
                    try {
                        lm.requestLocationUpdates(android.location.LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                        myLocation = lm.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER);
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

    /**
     * the function check if the given premission is enough to try to get phone's location
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                if (requestCode == 5){
                        if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                                getLocation();
                        }
                }
        }

    /**
     * the function convert address to location
     * @param my_address - the address string to convert
     * @return
     */
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

    /**
     * the function calculates distance between 2 given address
     * @param a first address
     * @param b second address
     * @return
     */
        public float calculateDistance(Location a, Location b) {

        if (a == null) return Float.MAX_VALUE;
                float distance = a.distanceTo(b);
                if(distance > 1000)
                {
                        distance = distance/1000;
                }
                return distance;

        }
    public static boolean canGetLocation() {
        return isLocationEnabled(); // application context
    }

    /**
     * check if the device location is on
     * @return
     */
    public static boolean isLocationEnabled() {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public void displayLocationSettingsRequest() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult((Activity)context, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

}

