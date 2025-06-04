package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.data.entity.Post;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public class adapter_home_pages extends RecyclerView.Adapter<adapter_home_pages.ViewHolder> {

    List<Post> postList;
    Context context;
    public adapter_home_pages(List<Post> postList, Context context) {
        this.postList = postList;
        this.context = context;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView clubName ;
        TextView contentPost;
        TextView timePost;
        ImageView imgPost;
        Button subcribePost;
        public ViewHolder(View itemView) {
            super(itemView);
            clubName = itemView.findViewById(R.id.clubName);
            contentPost = itemView.findViewById(R.id.contentPost);
            timePost = itemView.findViewById(R.id.timePost);
            imgPost = itemView.findViewById(R.id.imgPost);
            subcribePost = itemView.findViewById(R.id.subcribePost);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_page, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.clubName.setText(post.getNameClub());
        holder.contentPost.setText(post.getContent());
        holder.timePost.setText(post.getDeadline());
        Glide.with(holder.itemView.getContext())
                .load(new File(post.getImagePath()))
                .into(holder.imgPost);
        holder.subcribePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(post.getUrlJoin()));
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return postList.size();

    }

}
