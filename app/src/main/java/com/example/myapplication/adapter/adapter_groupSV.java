package com.example.myapplication.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.dao.UserDao;
import com.example.myapplication.data.entity.GvPost;
import com.example.myapplication.data.entity.User;

import java.util.List;

public class adapter_groupSV extends RecyclerView.Adapter<adapter_groupSV.ViewHolder> {
   List<GvPost> postsList;
    public adapter_groupSV(List<GvPost> postsList) {
        this.postsList = postsList;
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

    }


    @Override
    public int getItemCount() {
        return postsList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView Sv_img_post;
        TextView Sv_name, Sv_post;
        public ViewHolder(View itemView) {

            super(itemView);
            Sv_img_post = itemView.findViewById(R.id.Sv_img_post);
            Sv_name = itemView.findViewById(R.id.SvName);
            Sv_post = itemView.findViewById(R.id.Sv_post);
        }
    }
}
