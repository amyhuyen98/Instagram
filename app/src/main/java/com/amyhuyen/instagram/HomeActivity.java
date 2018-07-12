package com.amyhuyen.instagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.amyhuyen.instagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String AUTHORITY = "com.amyhuyen.instagram";
    private File photoFile;

    // the views
    public @BindView (R.id.etDescription) EditText etDescription;
    public @BindView (R.id.ivPhoto) ImageView ivPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // bind the views using butterknife
        ButterKnife.bind(this);

        loadTopPosts();
    }

    // method to create and save posts
    private void createPost(String description, ParseFile imageFile, ParseUser user){
        final Post newPost = new Post();
        newPost.setDescription(description);
        newPost.setImage(imageFile);
        newPost.setUser(user);

        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    Log.d("HomeActivity", "Create post success!");
                    Toast.makeText(HomeActivity.this, "Posted", Toast.LENGTH_SHORT).show();
                } else{
                    e.printStackTrace();
                }
            }
        });
    }

    // method to load the top posts
    private void loadTopPosts(){
        final Post.Query postsQuery = new Post.Query();
        postsQuery.getTop().withUser();

        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null){
                    for (int i = 0; i<objects.size(); i++){
                        Log.d("HomeActivity", "Post[" + i + "] = "
                                + objects.get(i).getDescription()
                                + "\nusername = " + objects.get(i).getUser().getUsername());
                    }
                } else{
                    e.printStackTrace();
                }
            }
        });
    }

    // on click for logout button using butterknife
    @OnClick(R.id.btnLogout)
    public void onLogoutClick(){
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null){Toast.makeText(this, "Successfully logged out", Toast.LENGTH_SHORT).show();}
        // get back to login screen
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    // on click for picture button using butterknife
    @OnClick(R.id.btnPicture)
    public void onPictureClick(){
        File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            photoFile = File.createTempFile("photo", ".jpg", directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dispatchTakePictureIntent();
    }

    // on click for create post button using butterknife
    @OnClick(R.id.btnCreate)
    public void onCreateClick(){
        final String description = etDescription.getText().toString();
        final ParseUser user = ParseUser.getCurrentUser();

        if (photoFile != null ){
            final ParseFile parseFile = new ParseFile(photoFile);
            parseFile.saveInBackground(new SaveCallback(){
                @Override
                public void done(ParseException e){
                    createPost(description, parseFile, user);
                }
            });
        } else {
            Toast.makeText(HomeActivity.this, "Please take a picture", Toast.LENGTH_SHORT).show();
        }
    }

    // on click for refresh posts button using butterknife
    @OnClick(R.id.btnRefresh)
    public void onRefreshClick(){
        loadTopPosts();
    }

    //on click for timeline button using butterknife
    @OnClick(R.id.btnTimeline)
    public void onTimelineClick(){
        Intent intent = new Intent(HomeActivity.this, TimelineActivity.class);
        startActivity(intent);
    }


    // picture intent
    private void dispatchTakePictureIntent(){
        Uri uri = FileProvider.getUriForFile(this, AUTHORITY, photoFile);
        // intent to take picture and return control to the calling application
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    // on activity result method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap imageBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            ivPhoto.setImageBitmap(imageBitmap);
        }
    }

}
