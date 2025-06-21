package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

import java.util.List;

public class adapter_detail_user extends RecyclerView.Adapter<adapter_detail_user.ViewHolder> {
    List<String> ListImg;
    Context context;
    boolean isOwner;
    OnImageClickListener listener;
    public interface OnImageClickListener {
        void onImageClick(int position);
    }
    public adapter_detail_user(List<String> ListImg,Context context,  boolean isOwner, OnImageClickListener listener) {
        this.ListImg = ListImg;
        this.context = context;
        this.isOwner = isOwner;
        this.listener = listener;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
        }
    }
    @NonNull
    @Override
    public adapter_detail_user.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_detail_user.ViewHolder holder, int position) {
        String imageUrl = ListImg.get(position);
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.img_1)
                .into(holder.img);

        if (isOwner) {
            holder.img.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onImageClick(position);
                }
            });
        } else {
            holder.img.setOnClickListener(null);  // Không cho click nếu không phải owner
        }
    }


    @Override
    public int getItemCount() {
        return 6;
    }
}
