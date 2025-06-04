package com.example.myapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.AppDatabase;
import com.example.myapplication.data.dao.CommentDao;
import com.example.myapplication.data.dao.LikeDao;
import com.example.myapplication.data.dao.UserDao;
import com.example.myapplication.data.entity.Comment;
import com.example.myapplication.data.entity.GvPost;
import com.example.myapplication.data.entity.Like;
import com.example.myapplication.data.entity.User;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.logging.Handler;

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
        Executors.newSingleThreadExecutor().execute(() -> {
            Like existingLike = likeDao.getUserLikeForPost(post.getId(), currentUserId);
            int likeCount = likeDao.getLikeCount(post.getId());
            holder.iconLike.post(() -> {
                if (existingLike != null) {
                    holder.iconLike.setColorFilter(Color.RED);  // Đổi màu khi đã like

                } else {
                    holder.iconLike.setColorFilter(Color.GRAY); // Đổi màu khi chưa like
                }
                holder.SoLuong.setText(likeCount + " likes");
            });
        });
        holder.iconLike.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "đã ấn", Toast.LENGTH_SHORT).show();
            if (likeClickListener != null) {
                likeClickListener.onLikeClick(post, position);
            }
        });

        holder.btn_comment.setOnClickListener(view -> {
            loadComment(view.getContext());
        });
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView Sv_img_post, iconLike, btn_comment;
        TextView Sv_name, Sv_post, SoLuong;

        public ViewHolder(View itemView) {

            super(itemView);
            Sv_img_post = itemView.findViewById(R.id.Sv_img_post);
            Sv_name = itemView.findViewById(R.id.SvName);
            Sv_post = itemView.findViewById(R.id.Sv_post);
            iconLike = itemView.findViewById(R.id.iconLike);
            SoLuong = itemView.findViewById(R.id.SoLuong);
            btn_comment = itemView.findViewById(R.id.btn_comment);
        }
    }

    public void loadComment(Context context){
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        View view1 = LayoutInflater.from(dialog.getContext()).inflate(R.layout.popup_comment, null);
        dialog.setContentView(view1);
        EditText edt_Comment = view1.findViewById(R.id.edt_Comment);
        Button btn_Send = view1.findViewById(R.id.btn_Send);
        RecyclerView recycler_Comment = view1.findViewById(R.id.recycler_Comment);
        recycler_Comment.setLayoutManager(new LinearLayoutManager(view1.getContext()));
        adapter_comment adapterComment = new adapter_comment();
        Executors.newSingleThreadExecutor().execute(()->{
            AppDatabase db = AppDatabase.getDatabase(view1.getContext());
            CommentDao commentDao = db.commentDao();
            List<Comment> commentList = commentDao.getAllComments();
            Collections.reverse(commentList);
            adapterComment.setData(commentList);
            recycler_Comment.setAdapter(adapterComment);
        });
        dialog.show();
        btn_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newComment = edt_Comment.getText().toString();
                if (!newComment.isEmpty()){
                    updateContent(view.getContext(), newComment);
                    dialog.dismiss();
                }
            }
        });
    }

    public void updateContent(Context context, String newComment){
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        View view1 = LayoutInflater.from(dialog.getContext()).inflate(R.layout.popup_comment, null);
        dialog.setContentView(view1);
        EditText edt_Comment = view1.findViewById(R.id.edt_Comment);
        Button btn_Send = view1.findViewById(R.id.btn_Send);
        RecyclerView recycler_Comment = view1.findViewById(R.id.recycler_Comment);
        recycler_Comment.setLayoutManager(new LinearLayoutManager(view1.getContext()));
        adapter_comment adapterComment = new adapter_comment();
        Executors.newSingleThreadExecutor().execute(()->{
            AppDatabase db = AppDatabase.getDatabase(view1.getContext());
            CommentDao commentDao = db.commentDao();
            Comment newcomment = new Comment(newComment);
            commentDao.insert(newcomment);
            List<Comment> commentList = commentDao.getAllComments();
            for(Comment comment : commentList){
                Log.d("Comment", comment.getContent());
            }
            Collections.reverse(commentList);
            adapterComment.setData(commentList);
            if(adapterComment!= null){
                Log.d("adapter", "null");
            }
            recycler_Comment.setAdapter(adapterComment);
        });
        dialog.show();
        btn_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newComment = edt_Comment.getText().toString();
                if (!newComment.isEmpty()){
                    updateContent(view.getContext(), newComment);
                    dialog.dismiss();
                }
            }
        });
    }
}
