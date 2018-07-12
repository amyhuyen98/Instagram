package com.amyhuyen.instagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.amyhuyen.instagram.model.Post;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class CameraFragment extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String AUTHORITY = "com.amyhuyen.instagram";
    private File photoFile;

    // the views
    @BindView(R.id.btnPicture) Button btnPicture;
    @BindView(R.id.ivPhoto) ImageView ivPhoto;
    @BindView(R.id.etDescription) EditText etDescription;
    @BindView(R.id.btnCreate) Button btnCreate;
    @BindView(R.id.tilDescription) TextInputLayout tilDescription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        // define the xml file for the fragment
        return inflater.inflate(R.layout.fragment_camera, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        // bind the views using butterknife
        ButterKnife.bind(this, view);
    }

    // on click for picture button using butterknife
    @OnClick(R.id.btnPicture)
    public void onPictureClick(){
        File directory = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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
            Toast.makeText(getActivity(), "Please take a picture", Toast.LENGTH_SHORT).show();
        }
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
                    Log.d("Landing Activity", "Create post success!");
                    Toast.makeText(getActivity(), "Posted", Toast.LENGTH_SHORT).show();

                    // switch to timeline fragment
                    Fragment fragment = new TimelineFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.flContainer, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                } else{
                    e.printStackTrace();
                }
            }
        });
    }

    // picture intent
    private void dispatchTakePictureIntent(){
        Uri uri = FileProvider.getUriForFile(getActivity(), AUTHORITY, photoFile);
        // intent to take picture and return control to the calling application
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null){
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    // on activity result method
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            // set create post views visible
            btnCreate.setVisibility(View.VISIBLE);
            tilDescription.setVisibility(View.VISIBLE);
            ivPhoto.setVisibility(View.VISIBLE);

            // hide take photo button
            btnPicture.setVisibility(View.GONE);

            Bitmap imageBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            ivPhoto.setImageBitmap(imageBitmap);
        }
    }



}