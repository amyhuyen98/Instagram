package com.amyhuyen.instagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.amyhuyen.instagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    // the views
    public @BindView (R.id.etDescription) EditText etDescription;
    public @BindView (R.id.btnCreate) Button btnCreate;
    public @BindView (R.id.btnRefresh) Button btnRefresh;
    public @BindView (R.id.btnPicture) Button btnPicture;
    public @BindView (R.id.ivPhoto) ImageView ivPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // bind the views using butterknife
        ButterKnife.bind(this);

        // on click listener for create button
        btnCreate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final String description = etDescription.getText().toString();
                final ParseUser user = ParseUser.getCurrentUser();
                createPost(description, null, user);
            }
        });

        // on click listener for refresh button
        btnRefresh.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                loadTopPosts();
            }
        });

        // on click listener for capture image button
        btnPicture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dispatchTakePictureIntent();
            }
        });

        loadTopPosts();
    }

    // method to create and save posts
    private void createPost(String description, ParseFile imageFile, ParseUser user){
        final Post newPost = new Post();
        newPost.setDescription(description);
//        newPost.setImage(imageFile);
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

    // picture intent
    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    // on activity result method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ivPhoto.setImageBitmap(imageBitmap);
        }
    }

}
