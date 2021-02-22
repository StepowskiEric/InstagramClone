package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    private File photoFile;
    private ImageView postImage;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public ArrayList<Post> posts = new ArrayList<>();


    BottomNavigationView btmNavView;
    CoordinatorLayout toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btmNavView = findViewById(R.id.bottom_navigation);
        toolbar = findViewById(R.id.toolBar);
        postImage = findViewById(R.id.postImage);
        mRecyclerView = findViewById(R.id.rvPosts);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new PostAdapter(this,posts);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        queryPosts();


        btmNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add_picture:
                        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        photoFile = getPhotoFileUri(photoFileName);
                        Uri fileProvider = FileProvider.getUriForFile(MainActivity.this, "com.codepath.fileprovider", photoFile);
                        i.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

                        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
                        // So as long as the result is not null, it's safe to use the intent.
                        if (i.resolveActivity(getPackageManager()) != null) {
                            // Start the image capture intent to take photo
                            startActivityForResult(i, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                            startActivity(i);
                            return true;
                        }
                    case R.id.profile:
                        Intent j = new Intent(MainActivity.this, LogoutActivity.class);
                        startActivity(j);
                        return true;
                }

                return false;
            }


            public File getPhotoFileUri(String fileName) {
                // Get safe storage directory for photos
                // Use `getExternalFilesDir` on Context to access package-specific directories.
                // This way, we don't need to request external read/write runtime permissions.
                File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

                // Create the storage directory if it does not exist
                if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
                    Log.d(TAG, "failed to create directory");
                }

                // Return the file target for the photo based on filename
                File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

                return file;
            }
        });
    }

    public void savePost(String description, ParseUser currentUser, File photoFile) {
        Post post = new Post();
        post.setDescription(description);
        post.setImage(new ParseFile(photoFile));
        post.setUser(currentUser);
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(MainActivity.this, "Error while saving!", Toast.LENGTH_LONG).show();
                }
                Log.i(TAG, "Post save was successful");
                posts.add(post);
                mAdapter = new PostAdapter(MainActivity.this,posts);
                mRecyclerView = findViewById(R.id.rvPosts);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyItemInserted(0);

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                Bitmap bMapScaled = Bitmap.createScaledBitmap(takenImage, 300, 300, true);


                Log.i(TAG, "Successfully took pic");
                Intent i = new Intent(MainActivity.this, PictureActivity.class);;
                i.putExtra("BitmapImage", bMapScaled);
                i.putExtra("photoFile", photoFile);
                startActivity(i);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }




        public void queryPosts () {
            final List<Post> data = new ArrayList<>();

            ParseQuery<Post> query = new ParseQuery<>("Post");
            query.orderByDescending("createdAt");
            query.include(Post.KEY_USER);
            query.findInBackground(new FindCallback<Post>() {
                @Override
                public void done(List<Post> list, ParseException e) {
                    if(e == null)
                    {
                        for(Post posts : list)
                        {
                            Post postList = new Post();
                            postList.setDescription(posts.getDescription());
                            postList.setImage(posts.getImage());
                            postList.setUser(posts.getUser());

                            data.add(postList);
                        }

                        mAdapter = new PostAdapter(MainActivity.this,data);
                        mRecyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
                }





}

