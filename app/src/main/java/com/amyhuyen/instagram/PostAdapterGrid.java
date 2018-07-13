package com.amyhuyen.instagram;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amyhuyen.instagram.model.Post;
import com.codepath.apps.restclienttemplate.models.GlideApp;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostAdapterGrid extends RecyclerView.Adapter<PostAdapterGrid.ViewHolder> {

    // declare variables
    private List<Post> mPosts;
    Activity context;

    public PostAdapterGrid(List<Post> posts, Activity activity) {
        mPosts = posts;
        context = activity;
    }

    // for each row, inflate the layout and cache references into ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.item_post_grid, parent, false);
        ViewHolder viewHolder = new ViewHolder(postView);
        return viewHolder;
    }

    // bind the values based on the position of the eleme t
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get the data according to position
        final Post post = mPosts.get(position);

        // getting and displaying images
        if (post.getImage() != null) {
            String url = post.getImage().getUrl();
            GlideApp.with(context)
                    .load(url)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.ivImage);
        }
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public @BindView(R.id.tvHandle)
        TextView tvHandle;
        public @BindView(R.id.tvCaption)
        TextView tvCaption;
        public @BindView(R.id.ivProfileImage)
        ImageView ivProfileImage;
        public @BindView(R.id.ivImage)
        ImageView ivImage;
        public @BindView(R.id.tvTimeStamp)
        TextView tvTimeStamp;

        public ViewHolder(View itemView) {
            super(itemView);
            // bind views using butterknife
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // get item position
            int position = getAdapterPosition();
            // make sure the position is valid
            if (position != RecyclerView.NO_POSITION) {
                // get the post at the position
                Post post = mPosts.get(position);
                // create intent for new activity
                Intent intent = new Intent(context, PostDetails.class);
                intent.putExtra("postId", post.getObjectId());
                context.startActivity(intent);
            }
        }
    }

        // clean all elements of the recycler
        public void clear() {
            mPosts.clear();
            notifyDataSetChanged();
        }

        // add a list of all items
        public void addAll(List<Post> list) {
            mPosts.addAll(list);
            notifyDataSetChanged();
        }
    }
