package com.amyhuyen.instagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    // the views in our login screen
    public @BindView (R.id.etUsername)EditText etUsername;
    public @BindView (R.id.etPassword)EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // check if user is already logged in
        ParseUser user = ParseUser.getCurrentUser();
        if (user != null){
            Intent intent = new Intent(this, LandingActivity.class);
            startActivity(intent);
            finish();
        }

        // bind the views using butterknife
        ButterKnife.bind(this);
    }

    // on click for login button using butterknife
    @OnClick(R.id.btnLogin)
    public void onLoginClick(){
        if (ParseUser.getCurrentUser() != null){
            ParseUser.logOut();
        }
        // access username and password input
        final String username = etUsername.getText().toString();
        final String password = etPassword.getText().toString();

        // login use username and password
        login(username, password);
    }

    // on click for signup button using butterknife
    @OnClick(R.id.btnSignUp)
    public void onSignUpClickMain(){
        signUp();
    }

    // method that logs a user in
    private void login(String username, String password){
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null){
                    Log.d("LogInActivity", "Login successful!");

                    // go to home activity after successful login
                    final Intent intent = new Intent(MainActivity.this, LandingActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Log.e("LogInActivity", "Login failure.");
                    e.printStackTrace();
                }
            }
        });
    }

    // method that signs a user up for an account
    private void signUp(){
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intent);
    }
}
