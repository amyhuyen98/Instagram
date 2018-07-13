package com.amyhuyen.instagram;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amyhuyen.instagram.model.Post;
import com.amyhuyen.instagram.model.TimeFormatter;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.codepath.apps.restclienttemplate.models.GlideApp;
import com.parse.GetCallback;
import com.parse.ParseException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostDetails extends AppCompatActivity {

    // the views
    public @BindView (R.id.tvHandle2) TextView tvHandle2;
    public @BindView (R.id.tvCaption2) TextView tvCaption2;
    public @BindView (R.id.ivProfileImage2) ImageView ivProfileImage2;
    public @BindView (R.id.ivImage2) ImageView ivImage2;
    public @BindView (R.id.tvTimeStamp2) TextView tvTimeStamp2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        // bind views
        ButterKnife.bind(this);

        // action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        LayoutInflater inflater = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.action_bar, null);
        actionBar.setCustomView(v);

        // get post objectId from intent
        getPostInfo(getIntent().getStringExtra("postId"));
    }

    public void getPostInfo(String objectId){
        // get the post based on objectId
        final Post.Query query = new Post.Query();
        query.getTop().withUser();
        query.getInBackground(objectId, new GetCallback<Post>() {
            @Override
            public void done(Post object, ParseException e) {
                if (e == null){
                    // populate fields with information

                    if (!object.getDescription().equals("")){
                        SpannableString ss1=  new SpannableString(object.getHandle() + "  ");
                        ss1.setSpan(new StyleSpan(Typeface.BOLD), 0, ss1.length(), 0);
                        tvCaption2.append(ss1);
                        tvCaption2.append(object.getDescription());
                    } else{
                        tvCaption2.setText("");
                        tvCaption2.setVisibility(View.GONE);
                    }

                    tvHandle2.setText(object.getHandle());
                    if (object.getImage() != null){
                        GlideApp.with(PostDetails.this)
                                .load(object.getImage().getUrl())
                                .placeholder(R.drawable.placeholder)
                                .into(ivImage2);
                    }
                    if (object.getProfileImage() != null) {
                        GlideApp.with(PostDetails.this)
                                .load(object.getProfileImage().getUrl())
                                .transform(new CircleCrop())
                                .placeholder(R.drawable.instagram_user)
                                .into(ivProfileImage2);
                    }

                    tvTimeStamp2.setText(TimeFormatter.getTimeDifference(object.getCreatedAt().toString()));
                } else{ e.printStackTrace(); }
            }
        });
    }
}
