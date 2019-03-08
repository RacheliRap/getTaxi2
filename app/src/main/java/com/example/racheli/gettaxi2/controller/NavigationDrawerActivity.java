package com.example.racheli.gettaxi2.controller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.racheli.gettaxi2.R;

import java.util.zip.Inflater;

/**
 * navigation drawer class, handle the navigation drawer and click options.
 */
public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //automatically code
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("My Cab");
        getSupportActionBar().setTitle("My Cab");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    /**
     * The function handle open and close the drawer when back button is pressed
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * The function create the option menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_navigation_drawer, menu);
        LocationClass lc = new LocationClass(getApplication());
        //if device location is off
        if(!lc.canGetLocation())
        {
            showDialog();
        }
        return true;
    }

    /**
     * The function show the dialog for turn on the location on the phone
     */
    public void showDialog()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("ACCESS PHONE LOCATION");
        String message = "In order to use this app you must turn on device location";
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("Turn on",onClickListener);
        alertDialogBuilder.setNegativeButton("I prefer not use this app ",onClickListener);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    /**
     * listener for the dialog that show un function showDialog()
     */
    AlertDialog.OnClickListener onClickListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                //the user dosen't want to turn on location
                case Dialog.BUTTON_NEGATIVE: {
                    finishAffinity();
                    System.exit(0);
                    break;
                }
                //take the user to phone settings
                case Dialog.BUTTON_POSITIVE: {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                    break;
                }
            }
        }
    };

    /**
     * Handle menu on tool bar
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    /**
     * handle select options from the navigation drawer
     */
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();
        //available rides
        if (id == R.id.nav_availabe_rides) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new FirstFragment())
                    .addToBackStack(null).commit();
            //specific rides
        } else if (id == R.id.nav_specific_rides) {
            fragmentManager.beginTransaction().replace(R.id.content_frame , new SecondFragment())
            .addToBackStack(null).commit();
            //if the user press exit return the app to the login activity
        } else if (id == R.id.nav_exit) {
            //fragmentManager.beginTransaction().replace(R.id.content_frame , new ExitFragment()).commit();
            Intent intent = new Intent(this , MainActivity.class);
            startActivity(intent);
            //browse online for getTaxi website
        } else if (id == R.id.nav_browse_online) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://gett.com/il/about/"));
            startActivity(browserIntent);

        } else if (id == R.id.nav_contact_us) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            String [] address = {"support@myCab.com"} ;
            emailIntent.putExtra(Intent.EXTRA_EMAIL, address);
            startActivity(Intent.createChooser(emailIntent, ""));
        }
        else if( id == R.id.nav_share)
        {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.setType("message/rfc822");
            String body = "I would like the share with you the new amazing app - myCab." +
                    "\nFor more details contact Support@myCab.com";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"subject");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
