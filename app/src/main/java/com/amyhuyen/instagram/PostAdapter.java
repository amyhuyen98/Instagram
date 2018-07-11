package com.amyhuyen.instagram;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amyhuyen.instagram.model.Post;

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
        holder.tvCaption.setText(post.getDescription());
//        holder.tvHandle.setText(post.getUser().getString("handle"));

        //TODO - load images here into imageviews!!

    }

    @Override
    public int getItemCount() { return mPosts.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public @BindView (R.id.tvHandle) TextView tvHandle;
        public @BindView (R.id.tvCaption) TextView tvCaption;
        public @BindView (R.id.ivProfileImage) ImageView ivProfileImage;
        public @BindView (R.id.ivImage) ImageView ivImage;

        public ViewHolder(View itemView){
            super (itemView);

            // bind views using butterknife
            ButterKnife.bind(this, itemView);
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
