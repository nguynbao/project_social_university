package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.entity.Post;

import java.util.List;

public class adapter_home_pages extends RecyclerView.Adapter<adapter_home_pages.ViewHolder> {
    List<Post> postList;
    public adapter_home_pages( List<Post> postList) {
        this.postList = postList;
    }

    @NonNull
    @Override
    public adapter_home_pages.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_page, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.nameClub.setText(post.getNameClub());
        holder.content.setText(post.getContent());
        holder.time.setText(post.getDeadline());
        holder.imgPost.setImageResource(R.drawable.img_bai_dang);
    }


    @Override
    public int getItemCount() {
        return postList.size();

    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameClub, content, time;
        ImageView imgPost;
        AppCompatButton submit;
        public ViewHolder(View itemView) {
            super(itemView);
            nameClub = itemView.findViewById(R.id.nameClub);
            content = itemView.findViewById(R.id.content);
            time = itemView.findViewById(R.id.time);
            imgPost = itemView.findViewById(R.id.imgPost);
            submit = itemView.findViewById(R.id.submit);
        }
    }

}
