package com.amyhuyen.instagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileFragment extends Fragment {

    // the views
    @BindView (R.id.btnLogout) Button btnLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle SavedInstanceState){
        // definethe xml file for the fragment
        return inflater.inflate(R.layout.fragment_profile, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        // bind the views using butterknife
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.btnLogout)
    public void onLogoutClick() {
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getActivity(), "Successfully logged out", Toast.LENGTH_SHORT).show();
        }
        // get back to login screen
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }
}