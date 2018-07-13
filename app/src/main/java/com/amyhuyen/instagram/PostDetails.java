package com.amyhuyen.instagram;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.StyleSpan;
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
                    SpannableString ss1=  new SpannableString(object.getHandle() + " ");
                    ss1.setSpan(new StyleSpan(Typeface.BOLD), 0, ss1.length(), 0);
                    tvCaption2.append(ss1);
                    tvCaption2.append(object.getDescription());

                    tvHandle2.setText(object.getHandle());
                    tvCaption2.setText(object.getDescription());
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
                                .into(ivProfileImage2);
                    }

                    tvTimeStamp2.setText(TimeFormatter.getTimeDifference(object.getCreatedAt().toString()));
                } else{ e.printStackTrace(); }
            }
        });
    }
}
