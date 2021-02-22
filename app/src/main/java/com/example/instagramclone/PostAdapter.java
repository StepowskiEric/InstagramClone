package com.example.instagramclone;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ui.widget.ParseImageView;

import java.util.ArrayList;
import java.util.List;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    public List<Post> mPosts;
    Context mContext;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public PostAdapter(Context mContext, List<Post> posts) {
        this.mContext = mContext;
        this.mPosts = posts;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Post post = mPosts.get(position);
            TextView usernametxt = holder.username;
            TextView description = holder.description;
            ImageView postImage = holder.postImage;
            description.setText(post.getDescription());
            usernametxt.setText(post.getUser().getUsername());
            Glide.with(mContext).load(post.getImage().getUrl()).into(postImage);







    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView username;
        private ImageView postImage;
        private TextView description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            postImage = itemView.findViewById(R.id.postImage);
            description = itemView.findViewById(R.id.rvDescription);
            username = itemView.findViewById(R.id.rvUserName);



        }
    }
}
