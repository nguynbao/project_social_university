package com.example.myapplication.adapter;

import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.dao.LikeDao;
import com.example.myapplication.data.dao.UserDao;
import com.example.myapplication.data.entity.GvPost;
import com.example.myapplication.data.entity.Like;
import com.example.myapplication.data.entity.User;

import java.util.List;
import java.util.concurrent.Executors;

public class adapter_groupSV extends RecyclerView.Adapter<adapter_groupSV.ViewHolder> {
   List<GvPost> postsList;
    private LikeDao likeDao;
    private int currentUserId;
    public interface OnLikeClickListener {
        void onLikeClick(GvPost post, int position);
    }
    private final OnLikeClickListener likeClickListener;
    public adapter_groupSV(List<GvPost> postsList, OnLikeClickListener likeClickListener, LikeDao likeDao, int currentUserId) {
        this.postsList = postsList;
        this.likeClickListener = likeClickListener;
        this.likeDao = likeDao;
        this.currentUserId = currentUserId;
    }


    public void setData(List<GvPost> data) {
        this.postsList = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public adapter_groupSV.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_sv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GvPost post = postsList.get(position);
        holder.Sv_name.setText(post.getStudentName());
        holder.Sv_post.setText(post.getContent());
        holder.Sv_img_post.setImageURI(post.getImageUri());
        // Check Like status in background
        Executors.newSingleThreadExecutor().execute(() -> {
            Like existingLike = likeDao.getUserLikeForPost(post.getId(), currentUserId);
            holder.iconLike.post(() -> {
                if (existingLike != null) {
                    holder.iconLike.setColorFilter(Color.RED);  // Đổi màu khi đã like
                } else {
                    holder.iconLike.setColorFilter(Color.GRAY); // Đổi màu khi chưa like
                }
            });
        });
        holder.iconLike.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "đã ấn", Toast.LENGTH_SHORT).show();
            if (likeClickListener != null) {
                likeClickListener.onLikeClick(post, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView Sv_img_post, iconLike;
        TextView Sv_name, Sv_post, SoLuong;

        public ViewHolder(View itemView) {

            super(itemView);
            Sv_img_post = itemView.findViewById(R.id.Sv_img_post);
            Sv_name = itemView.findViewById(R.id.SvName);
            Sv_post = itemView.findViewById(R.id.Sv_post);
            iconLike = itemView.findViewById(R.id.iconLike);
            SoLuong = itemView.findViewById(R.id.SoLuong);
        }
    }
}
