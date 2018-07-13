package com.amyhuyen.instagram;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LandingActivity extends AppCompatActivity {

    @BindView (R.id.bottom_nav) BottomNavigationView bottomNavigationView;
    public ParseUser neededUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        // bind views using butterknife
        ButterKnife.bind(this);

        // action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        LayoutInflater inflater = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.action_bar, null);
        actionBar.setCustomView(v);

        final FragmentManager fragmentManager = getSupportFragmentManager();

        // my fragments
        final Fragment timelineFrag = new TimelineFragment();
        final Fragment cameraFrag = new CameraFragment();
        final Fragment profileFrag = new ProfileFragment();
        final Fragment userFrag = new UserFragment();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContainer, timelineFrag);
        fragmentTransaction.commit();

        // handle the navigation selection
        bottomNavigationView.setOnNavigationItemSelectedListener(
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                 @Override
                 public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                     Fragment selectedFragment = null;
                     // define all the possible fragment situations
                     switch(menuItem.getItemId()){
                         case R.id.action_search:
                             selectedFragment = userFrag;
                             break;
                         case R.id.action_camera:
                             selectedFragment = cameraFrag;
                             break;
                         case R.id.action_profile:
                             selectedFragment = profileFrag;
                             break;
                         case R.id.action_timeline:
                             selectedFragment = timelineFrag;
                             break;
                     }

                     // handle the fragment transaction
                     FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                     fragmentTransaction.replace(R.id.flContainer, selectedFragment);
                     fragmentTransaction.commit();
                     return true;
                 }
             });
    }

    public void showDetails(ParseUser user){
        neededUser = user;
        bottomNavigationView.setSelectedItemId(R.id.action_search);
        Fragment userFrag = new UserFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.add(R.id.flContainer, userFrag);
        fragmentTransaction.commit();
    }

    public ParseUser getNeededUser() {
        return neededUser;
    }
}
