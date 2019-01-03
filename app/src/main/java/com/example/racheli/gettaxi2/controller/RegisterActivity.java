package com.example.racheli.gettaxi2.controller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.racheli.gettaxi2.R;
import com.example.racheli.gettaxi2.model.backend.Backend;
import com.example.racheli.gettaxi2.model.backend.BackendFactory;
import com.example.racheli.gettaxi2.model.datasource.Action;
import com.example.racheli.gettaxi2.model.entities.Driver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private EditText fullNameEditext;
    private EditText passwordEditext;
    private EditText idEditext;
    private EditText phoneNumberEditext;
    private EditText emailEditext;
    private EditText ccNumberEditext;
    private Button registerButton;

    /**
     * Find the Views in the layout
     */
    private void findViews() {

        fullNameEditext = (EditText)findViewById( R.id.fullName );
        passwordEditext = (EditText)findViewById( R.id.password_editext );
        idEditext = (EditText)findViewById( R.id.id );
        phoneNumberEditext = (EditText)findViewById( R.id.phoneNumber );
        emailEditext = (EditText)findViewById( R.id.email );
        ccNumberEditext = (EditText)findViewById( R.id.ccNumber );
        registerButton = (Button)findViewById( R.id.register_button );

        registerButton.setOnClickListener( this );
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViews();
        initTextChangeListener();

    }
    @Override
    public void onClick(View v)
    {
        if(v == registerButton)
        {
            addDriver();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("email" ,emailEditext.getText().toString() );
            intent.putExtra("password" ,passwordEditext.getText().toString() );
            startActivity(intent);
            //register(emailEditext.getText().toString() , passwordEditext.getText().toString());
        }
    }

    public void addDriver()
    {
        Driver driver = getDriver();
        try{
            //String jsonObj = quickParse(ride);
            Backend instance = BackendFactory.getInstance();
            instance.addDriver(driver, new Action<String>() {
                @Override
                public void onSuccess(String obj) {
                    Toast.makeText(getBaseContext(), "Succeeded" + obj, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Exception exception) {
                    Toast.makeText(getBaseContext(), "Error \n" + exception.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onProgress(String status, double percent) {
                    if (percent != 100)
                        registerButton.setEnabled(false);
                   // addProgressBar.setProgress((int) percent);
                }
            });
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Error ", Toast.LENGTH_LONG).show();
            //resetView();
        }

    }

    /**
     * Initialize Driver object with the input in the text views
     * @return the initialized driver
     */
    public Driver getDriver()
    {
        Driver driver = new Driver();
        //initalize driver with the user input
        driver.setFullName(fullNameEditext.getText().toString());
        driver.setPassword(passwordEditext.getText().toString());
        driver.setId(idEditext.getText().toString());
        driver.setEmail(emailEditext.getText().toString());
        driver.setcCardNumber(ccNumberEditext.getText().toString());
        driver.setPhoneNumber(phoneNumberEditext.getText().toString());

        return driver;
    }

    public void initTextChangeListener() {
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
        fullNameEditext.setOnFocusChangeListener(onFocusChangeListener);
        passwordEditext.setOnFocusChangeListener(onFocusChangeListener);
        idEditext.setOnFocusChangeListener(onFocusChangeListener);
        ccNumberEditext.setOnFocusChangeListener(onFocusChangeListener);
        emailEditext.setOnFocusChangeListener(onFocusChangeListener);
        phoneNumberEditext.setOnFocusChangeListener(onFocusChangeListener);
    }

    /**
     * function checks if all the inputs are correct
     */
    private void validate() {
        boolean isAllValid = true;

        //checking for name correctness. if there is no space in the string, the user typed only first/last name.
        if(fullNameEditext.getText().toString().length() > 0) {
            if (!fullNameEditext.getText().toString().contains(" ")) {
                fullNameEditext.setError("must contain first and last name!");
                isAllValid = false;
            }
        }
        //checking for password correctness. if the password has less then 8 characters
        if(passwordEditext.getText().toString().length() > 0) {
            if (passwordEditext.getText().toString().length() < 8) {
                passwordEditext.setError("password must contain at least 8 characters!");
                isAllValid = false;
            }
        }
        //checking correctness of email address
        if (emailEditext.getText().length()>0){
            String email = emailEditext.getText().toString();
            String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
            java.util.regex.Matcher m = p.matcher(email);
            if (!m.matches()) {
                emailEditext.setError("Invalid email address");
                isAllValid = false;
            }
        }
        //checking correctness of phone number
        if (phoneNumberEditext.getText().length()>0){
            if(phoneNumberEditext.getText().toString().length() != 10) {
                phoneNumberEditext.setError("Invalid phone number");
                isAllValid = false;
            }
        }
        //checking correctness of creditcard format
        if (ccNumberEditext.getText().length()>0) {
            if(ccNumberEditext.getText().toString().length() != 16) {
                ccNumberEditext.setError("Invalid credit card number");
                isAllValid = false;
            }
        }

        //enables adRide Button only if all the textboxes are filled in
        if(fullNameEditext.getText().length() == 0 || passwordEditext.getText().length() == 0 ||
                ccNumberEditext.getText().length() == 0 || phoneNumberEditext.getText().length() == 0
                || emailEditext.getText().length() == 0 || idEditext.getText().length() == 0 )
        {
            isAllValid = false;
        }
        registerButton.setEnabled(isAllValid);
    }

    private void register(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            //call the function to let the user in the app
                            getIn();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getBaseContext(), "Register Failed. Please try again.", Toast.LENGTH_LONG).show();
                        }
                    }
                }); }

    /**
     * if the register passed ok, the function pass the user into the app - the next activity
     */
    private void getIn() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("email" ,emailEditext.getText().toString() );
        intent.putExtra("password" ,passwordEditext.getText().toString() );
        startActivity(intent);

    }


}

