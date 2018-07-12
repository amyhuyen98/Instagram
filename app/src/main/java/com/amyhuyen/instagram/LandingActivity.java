package com.amyhuyen.instagram;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LandingActivity extends AppCompatActivity {

    @BindView (R.id.bottom_nav) BottomNavigationView bottomNavigationView;
//    @BindView (R.id.miActionProgress) MenuItem miActionProgressItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        // bind views using butterknife
        ButterKnife.bind(this);

        final FragmentManager fragmentManager = getSupportFragmentManager();

        // my fragments
        final Fragment timelineFrag = new TimelineFragment();
        final Fragment cameraFrag = new CameraFragment();
        final Fragment profileFrag = new ProfileFragment();

        // handle the navigation selection
        bottomNavigationView.setOnNavigationItemSelectedListener(
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                 @Override
                 public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                     Fragment selectedFragment = null;

                     // define all the possible fragment situations
                     switch(menuItem.getItemId()){
                         case R.id.action_timeline:
                             selectedFragment = timelineFrag;
                             break;
                         case R.id.action_camera:
                             selectedFragment = cameraFrag;
                             break;
                         case R.id.action_profile:
                             selectedFragment = profileFrag;
                             break;
                     }

                     // handle the fragment transaction
                     FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                     fragmentTransaction.replace(R.id.flContainer, selectedFragment);
                     fragmentTransaction.commit();
                     return true;
                 }
             });

        // manually displaying the first fragment (just one time for the beginning)
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContainer, timelineFrag);
        fragmentTransaction.commit();
    }
//
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        // Store instance of the menu item containing progress
//        miActionProgressItem = menu.findItem(R.id.miActionProgress);
//        // Extract the action-view from the menu item
//        ProgressBar v =  (ProgressBar) MenuItemCompat.getActionView(miActionProgressItem);
//        // Return to finish
//        return super.onPrepareOptionsMenu(menu);
//    }
//
//    public void showProgressBar() {
//        // Show progress item
//        miActionProgressItem.setVisible(true);
//    }
//
//    public void hideProgressBar() {
//        // Hide progress item
//        miActionProgressItem.setVisible(false);
//    }
}
