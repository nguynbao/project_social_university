package com.example.myapplication.adapter;

import static android.os.Build.VERSION_CODES.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.entity.Post;
import com.example.myapplication.ui_admin.admin_editPost;

import java.util.List;

public class adapter_AllPost extends RecyclerView.Adapter<adapter_AllPost.ViewHolder> {
    List<Post> postList;
    Context context;

    public adapter_AllPost(List<Post> postList, Context context) {
        this.postList = postList;
        this.context = context;
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
    public void onBindViewHolder(@NonNull adapter_AllPost.ViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.textView.setText(post.getNameClub());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), admin_editPost.class);
                intent.putExtra("id_Post", post.getId());
                context.startActivity(intent);
                Activity activity = (Activity) holder.itemView.getContext();
                activity.finish();

            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}
