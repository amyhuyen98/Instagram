package com.amyhuyen.instagram;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amyhuyen.instagram.model.Post;
import com.amyhuyen.instagram.model.TimeFormatter;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.codepath.apps.restclienttemplate.models.GlideApp;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    // declare variables
    private List<Post> mPosts;
    Context context;

    public PostAdapter(List<Post> posts){
        mPosts = posts;
    }

    // for each row, inflate the layout and cache references into ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.item_post, parent, false);
        ViewHolder viewHolder = new ViewHolder(postView);
        return viewHolder;
    }

    // bind the values based on the position of the eleme t
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get the data according to position
        final Post post = mPosts.get(position);
        // populate the views according to this data

        SpannableString ss1=  new SpannableString(post.getHandle() + "  ");
        ss1.setSpan(new StyleSpan(Typeface.BOLD), 0, ss1.length(), 0);
        holder.tvCaption.setText("");
        holder.tvCaption.append(ss1);
        holder.tvCaption.append(post.getDescription());

        holder.tvHandle.setText(post.getHandle());
        holder.tvTimeStamp.setText(TimeFormatter.getTimeDifference(post.getCreatedAt().toString()));

        // getting and displaying images
        if (post.getImage() != null) {
            String url = post.getImage().getUrl();
            GlideApp.with(context)
                    .load(url)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.ivImage);
        }

        // getting and displaying profile images
        if (post.getProfileImage() != null){
            String url = post.getProfileImage().getUrl();
            GlideApp.with(context)
                    .load(url)
                    .transform(new CircleCrop())
                    .into(holder.ivProfileImage);
        }
    }

    @Override
    public int getItemCount() { return mPosts.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public @BindView (R.id.tvHandle) TextView tvHandle;
        public @BindView (R.id.tvCaption) TextView tvCaption;
        public @BindView (R.id.ivProfileImage) ImageView ivProfileImage;
        public @BindView (R.id.ivImage) ImageView ivImage;
        public @BindView (R.id.tvTimeStamp) TextView tvTimeStamp;

        public ViewHolder(View itemView){
            super (itemView);
            // bind views using butterknife
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // get item position
            int position = getAdapterPosition();
            // make sure the position is valid
            if (position != RecyclerView.NO_POSITION){
                // get the post at the position
                Post post = mPosts.get(position);
                //create intent for new activity
                Intent intent = new Intent(context, PostDetails.class);
                intent.putExtra("postId", post.getObjectId());
                context.startActivity(intent);
            }
        }
    }

    // clean all elements of the recycler
    public void clear(){
        mPosts.clear();
        notifyDataSetChanged();
    }

    // add a list of all items
    public void addAll(List<Post> list){
        mPosts.addAll(list);
        notifyDataSetChanged();
    }
}
