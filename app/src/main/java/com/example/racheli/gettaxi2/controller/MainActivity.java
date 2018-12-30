package com.example.racheli.gettaxi2.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.racheli.gettaxi2.R;
import com.example.racheli.gettaxi2.model.datasource.Firebase_DBManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private EditText emailEditext;
    private EditText passwordEditext;
    private Button loginButton;
    private Button registerButton;

    /**
     * Find the Views in the layout
     */

    private void findViews() {
        emailEditext = (EditText) findViewById(R.id.email_editext);
        passwordEditext = (EditText) findViewById(R.id.password_edittext);
        loginButton = (Button) findViewById(R.id.login_button);
        registerButton = (Button) findViewById(R.id.register_button);

        //sign up the buttons to Listener event
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        loadSharedPreferences();
        getRegisterData();
        initTextChangeListener();
        Firebase_DBManager f = new Firebase_DBManager();
        f.retrieveData();
    }

    /**
     * If the user just signed up, the function get the data from the RegisterActivity activity
     * and put it into the userName and password edit text
     */
    private void getRegisterData() {
        //get the intent that called this activity
        Intent intent = getIntent();
        //if extra data was sent from the calling activity
        if(this.getIntent().getExtras() != null)
        {
            //check if the data we are looking for was sent(email and password)
            if(this.getIntent().getExtras().containsKey("email") &&
                    this.getIntent().getExtras().containsKey("password") )
            {
                //assign into the text view the email and password
                emailEditext.setText(intent.getStringExtra("email"));
                passwordEditext.setText(intent.getStringExtra("password"));
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == loginButton) {
            //try to login using firebase function
            singIn(emailEditext.getText().toString(), passwordEditext.getText().toString());

        }
        if(v == registerButton)
        {
            //call new intent with the register activity
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }
    }

    private void initTextChangeListener() {
        //check if the focus has change in one of the edit text
        View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validate();
                }
            }
        };
        //set all edit text for onFocusChangeListener
        emailEditext.setOnFocusChangeListener(onFocusChangeListener);
        passwordEditext.setOnFocusChangeListener(onFocusChangeListener);

    }
    /**
     * check if the input in all text edits are valid, and if all are them are filled,
     * and enable/disable the login button
     **/
    private void validate() {
        boolean isAllValid = true;
        if(emailEditext.getText().length() == 0 || passwordEditext.getText().length() == 0)
        {
            isAllValid = false;
        }
        //Enable the button by the condition below
        loginButton.setEnabled(isAllValid);
    }

    /**
     * The function load the user saved userName and password to the UI.
     */
    private void loadSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.contains("NAME")) {
            emailEditext.setText(sharedPreferences.getString("NAME", null));
            Toast.makeText(this, "load name", Toast.LENGTH_SHORT).show();
        }
        if (sharedPreferences.contains("PASSWORD")) {
        //    String password = sharedPreferences.getInt("PASSWORD", 0);
            passwordEditext.setText(sharedPreferences.getString("PASSWORD", null));
            Toast.makeText(this, "load password", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * The function save the userName+password of the user on the phone,
     * so next time the user try to login, his details will be shown
     */
    private void saveSharedPreferences() {
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String name = emailEditext.getText().toString();
            String password = passwordEditext.getText().toString();
            editor.putString("NAME", name);
            editor.putString("PASSWORD", password);
            editor.commit();
            Toast.makeText(this, "save name and password Preferences", Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex)
        {
            Toast.makeText(this, "failed to save Preferences", Toast.LENGTH_SHORT).show();
        }
    }
    private void singIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            callIntent();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getBaseContext(), "incorrect email or password", Toast.LENGTH_SHORT).show();
                            loginButton.setEnabled(true); //set the button to enable other login
                        }
                    }
                });
    }

    private void callIntent() {
        //save the email and the password into shared preference
        saveSharedPreferences();
        //call new intent with the navigation drawer
        Intent intent = new Intent(this, NavigationDrawerActivity.class);
        startActivity(intent);
    }


}

