package com.amyhuyen.instagram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    // the views in our login screen
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // access username and password input
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();

                // login use username and password
                login(username, password);

            }
        });

    }

    // method that logs a user in
    private void login(String username, String password){
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null){
                    Log.d("LogInActivity", "Login successful!");
                } else {
                    Log.e("LogInActivity", "Login failure.");
                    e.printStackTrace();
                }
            }
        });
    }
}
