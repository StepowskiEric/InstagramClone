package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

import java.io.File;

public class PictureActivity extends AppCompatActivity {
    ImageView ivPreview;
    Button btnSubmit;
    EditText etDescription;
    BottomNavigationView btmNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        btmNavView = findViewById(R.id.bottom_navigation);
        ivPreview = (ImageView) findViewById(R.id.ivPreview);
        btnSubmit = findViewById(R.id.btnSubmit);
        etDescription = findViewById(R.id.etDescription);
        ActionBar actionBar = getSupportActionBar();
        MainActivity mainActivity = new MainActivity();
        actionBar.hide();
        Intent intent = getIntent();
        Bitmap bitmap = (Bitmap)  intent.getParcelableExtra("BitmapImage");
        ivPreview.setImageBitmap(bitmap);
        File photoFile = (File) intent.getExtras().get("photoFile");


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etDescription.getText().toString();
                if (description.isEmpty()) {
                    Toast.makeText(PictureActivity.this, "Description cannot be empty.", Toast.LENGTH_LONG).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                mainActivity.savePost(description, currentUser, photoFile);
                Intent i = new Intent(PictureActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        btmNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile:
                        Intent j = new Intent(PictureActivity.this, LogoutActivity.class);
                        startActivity(j);
                        return true;


                    case R.id.home:
                        Intent i = new Intent(PictureActivity.this, MainActivity.class);
                        startActivity(i);
                        return true;
                }

                return false;
            }
        });


    }
}