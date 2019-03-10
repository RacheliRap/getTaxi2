package com.example.racheli.gettaxi2.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.racheli.gettaxi2.R;
import com.example.racheli.gettaxi2.model.backend.Backend;
import com.example.racheli.gettaxi2.model.backend.BackendFactory;

/**
 * splash activity, wait till all the rides has benn read from fire base,
 * and than pass the user to the main activity
 */
public class SplashActivity extends Activity {
    Backend instance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        instance = BackendFactory.getInstance(getApplicationContext());

        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    //while rides are still being read from fire base
                    while(!instance.isComplete()) {
                        sleep(500);
                    }
                } catch (Exception e) {

                } finally {

                    Intent i = new Intent(SplashActivity.this,
                            MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        welcomeThread.start();
    }
}
