package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    private EditText tvUserName;
    private EditText tvPassword;
    private Button btnLogin;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }
        tvUserName = findViewById(R.id.tvUserName);
        tvPassword = findViewById(R.id.tvPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "OnClickLoginButton clicked");
                String username = tvUserName.getText().toString();
                String password = tvPassword.getText().toString();
                loginUser(username, password);


            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ParseUser user = new ParseUser();
                String username = tvUserName.getText().toString();
                String password = tvPassword.getText().toString();
                user.setUsername(username);
                user.setPassword(password);

                user.signUpInBackground(new SignUpCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(LoginActivity.this, "Successfully created account.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Cannot create account.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });



    }

    private void loginUser(String username, String password) {
        Log.i(TAG, "Attempting to login user " + username);
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with login", e);
                }
                goMainActivity();
                Toast.makeText(LoginActivity.this, "Successfully logged in.", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void goMainActivity() {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
    }
}