package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.data.entity.Comment;

import java.util.List;

public class adapter_comment extends RecyclerView.Adapter<adapter_comment.ViewHolder> {
    List<Comment> commentList;
    public adapter_comment(List<Comment> commentList){
        this.commentList = commentList;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_comment.ViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.textView.setText(comment.getContent());

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }


}
