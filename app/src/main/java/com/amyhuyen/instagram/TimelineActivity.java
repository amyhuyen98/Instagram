package com.amyhuyen.instagram;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.amyhuyen.instagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimelineActivity extends AppCompatActivity {

    @BindView(R.id.rvPosts) RecyclerView rvPosts;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    List<Post> posts;
    List<Post> newPosts;
    PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // bind views using butterknife
        ButterKnife.bind(this);
        // intialize the arraylist (data source)
        posts = new ArrayList<>();
        newPosts = new ArrayList<>();
        // construct the adapter from this data source
        postAdapter = new PostAdapter(posts);
        // recyclerview setup
        rvPosts.setLayoutManager(new LinearLayoutManager(this));
        // set the adapter
        rvPosts.setAdapter(postAdapter);

        getPosts();

        // swipe refresh
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPosts();
            }
        });

        // configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    public void getPosts(){
        // define the class to query
//        ParseQuery<Post> query = ParseQuery.getQuery(Post.class).include("user");
        Post.Query query = new Post.Query();
        query.getTop().withUser();
        // Execute the find asynchronously
        query.findInBackground(new FindCallback<Post>() {
            public void done(List<Post> itemList, ParseException e) {
                if (e == null) {
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
