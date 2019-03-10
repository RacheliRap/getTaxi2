package com.example.racheli.gettaxi2.controller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.ComponentName;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.racheli.gettaxi2.R;
import com.example.racheli.gettaxi2.model.entities.Driver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.zip.Inflater;

/**
 * navigation drawer class, handle the navigation drawer and click options.
 */
public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Driver mDriver;
    TextView welcome_textView;

    static ComponentName service = null;// service to see notification

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //automatically code
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("My Cab");
        getSupportActionBar().setTitle("My Cab");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if(this.getIntent().getExtras() != null)
        {
            //check if the data we are looking for was sent(email and password)
            if(this.getIntent().getExtras().containsKey("mDriver"))
            {
                mDriver = (Driver)(getIntent().getSerializableExtra("mDriver"));
                welcome_textView = findViewById(R.id.nav_drawer_textview);
                welcome_textView.setText(mDriver.getFullName() + ",\n Welcome back!");
            }
        }

        if (service == null) {//service does'nt start
            Intent intent = new Intent(getBaseContext(), NotificationService.class);//create new notification service intent
            service = startService(intent);// start the intent
        }
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
            showDialogForLocation();
        }
        //show welcome message dialog
        else
        {
            showDialog();
        }
        return true;
    }

    private void showDialog() {
       // Toast.makeText(getApplicationContext(), new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).format(Calendar.getInstance().getTime()), Toast.LENGTH_LONG).show();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("WELCOME " + mDriver.getFullName().toUpperCase());
        String message = "Welcome to our app:) \nFor more details press the menu button";
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setNeutralButton("Got it!",onClickListener);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    /**
     * The function show the dialog for turn on the location on the phone
     */
    public void showDialogForLocation()
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
                case  Dialog.BUTTON_NEUTRAL:
                {
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
        welcome_textView.setText("");
        FragmentManager fragmentManager = getFragmentManager();
        //available rides
        if (id == R.id.nav_availabe_rides) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("myDriver", mDriver );
            FirstFragment frag = new FirstFragment();
            frag.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.content_frame, frag).addToBackStack(null).commit();

           // fragmentManager.beginTransaction().replace(R.id.content_frame, new FirstFragment())
             //       .addToBackStack(null).commit();
            //specific rides
        } else if (id == R.id.nav_specific_rides) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("myDriver", mDriver );
            SecondFragment frag = new SecondFragment();
            frag.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.content_frame, frag).addToBackStack(null).commit();
            //fragmentManager.beginTransaction().replace(R.id.content_frame , new SecondFragment())
           // .addToBackStack(null).commit();
            //if the user press exit return the app to the login activity
        } else if (id == R.id.nav_exit) {
            //fragmentManager.beginTransaction().replace(R.id.content_frame , new ExitFragment()).commit();
            Intent intent = new Intent(this , MainActivity.class);
            startActivity(intent);
            //browse online for getTaxi website
        } else if (id == R.id.nav_browse_online) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://gett.com/il/about/"));
            startActivity(browserIntent);

        } /*else if (id == R.id.nav_contact_us) {
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
        }*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
