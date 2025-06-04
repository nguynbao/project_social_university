package com.example.myapplication.adapter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.entity.Post;
import com.example.myapplication.data.entity.User;
import com.example.myapplication.ui_admin.admin_editDoc;

import java.util.List;

public class adapter_bai_dang extends RecyclerView.Adapter<adapter_bai_dang.ViewHolder>{
    List<Post> postList;
    public adapter_bai_dang(List<Post> postList) {
        this.postList = postList;
    }
    public void setData(List<Post> newList) {
        this.postList = newList;
        notifyDataSetChanged();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView etName;
        public ViewHolder(@NonNull android.view.View itemView) {
            super(itemView);
            etName = itemView.findViewById(R.id.nameBD);

        }
    }

    @NonNull
    @Override
    public adapter_bai_dang.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_bai_dang, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_bai_dang.ViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.etName.setText(post.getContent());
        holder.etName.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), admin_editDoc.class);
            intent.putExtra("postID", post.getId());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}
