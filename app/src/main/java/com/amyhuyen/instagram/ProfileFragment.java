package com.amyhuyen.instagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.amyhuyen.instagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileFragment extends Fragment {

    // the views
    @BindView (R.id.btnLogout) Button btnLogout;
    @BindView(R.id.rvPosts) RecyclerView rvPosts;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    List<Post> posts;
    List<Post> newPosts;
    PostAdapterGrid postAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle SavedInstanceState){
        // define the xml file for the fragment
        return inflater.inflate(R.layout.fragment_profile, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        // bind the views using butterknife
        ButterKnife.bind(this, view);

        // intialize the arraylist (data source)
        posts = new ArrayList<>();
        newPosts = new ArrayList<>();
        // construct the adapter from this data source
        postAdapter = new PostAdapterGrid(posts, getActivity());
        // recyclerview setup
        rvPosts.setLayoutManager(new GridLayoutManager(getActivity(),3));
        // set the adapter
        rvPosts.setAdapter(postAdapter);

        getMyPosts();

        // swipe refresh
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMyPosts();
            }
        });

        // configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
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

    // method that uses query to populate the timeline with posts
    public void getMyPosts(){
        // define the class to query
        Post.Query query = new Post.Query();
        query.withUser();
        query.whereEqualTo("user", ParseUser.getCurrentUser());

        // Execute the find asynchronously
        query.findInBackground(new FindCallback<Post>() {
            public void done(List<Post> itemList, ParseException e) {
                if (e == null) {
                    newPosts.clear();
                    newPosts.addAll(itemList);
                    postAdapter.clear();
                    postAdapter.addAll(newPosts);
                    swipeContainer.setRefreshing(false);
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
            }
        });
    }
}