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

import com.bumptech.glide.Glide;
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
import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;

public class adapter_groupSV extends RecyclerView.Adapter<adapter_groupSV.ViewHolder> {
    List<GvPost> postsList;
    private LikeDao likeDao;
    private int currentUserId;
    public int countComment;
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
        Glide.with(holder.itemView.getContext()).load(new File(post.getImagePath())).into(holder.Sv_img_post);
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
            BottomSheetDialog dialog = new BottomSheetDialog(view.getContext());
            View view1 = LayoutInflater.from(dialog.getContext()).inflate(R.layout.popup_comment, null);
            dialog.setContentView(view1);

            RecyclerView recycler_Comment = view1.findViewById(R.id.recycler_Comment);
            recycler_Comment.setLayoutManager(new LinearLayoutManager(view1.getContext()));
            EditText edt_Comment = view1.findViewById(R.id.edt_Comment);
            Button btn_Send = view1.findViewById(R.id.btn_Send);

            int gvPost_id = post.getId();

            adapter_comment adapterComment = new adapter_comment();
            recycler_Comment.setAdapter(adapterComment);

            AppDatabase db = AppDatabase.getDatabase(view1.getContext());
            CommentDao commentDao = db.commentDao();

            loadComment(view1.getContext(), adapterComment, recycler_Comment,gvPost_id, () -> {
                holder.count_Comment.setText(adapterComment.getItemCount() + " comment");
            });

            btn_Send.setOnClickListener(sendView -> {
                String newComment = edt_Comment.getText().toString().trim();

                if (!newComment.isEmpty()) {
                    Executors.newSingleThreadExecutor().execute(() -> {
                        Comment comment = new Comment(newComment, gvPost_id);
                        commentDao.insert(comment);
                        loadComment(view1.getContext(), adapterComment, recycler_Comment, gvPost_id, () -> {
                            holder.count_Comment.setText(adapterComment.getItemCount() + " comment");
                        });
                    });
                    edt_Comment.post(() -> edt_Comment.setText(""));
                }
            });

            dialog.show();
        });
        Executors.newSingleThreadExecutor().execute(()->{
            AppDatabase db = AppDatabase.getDatabase(holder.itemView.getContext());
            CommentDao commentDao = db.commentDao();
            List<Comment> commentList = commentDao.getAllComments();
            int countComment1 = commentList.size();
            holder.count_Comment.setText(countComment1 + "comment");
        });
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView Sv_img_post, iconLike, btn_comment;
        TextView Sv_name, Sv_post, SoLuong, count_Comment;

        public ViewHolder(View itemView) {

            super(itemView);
            Sv_img_post = itemView.findViewById(R.id.Sv_img_post);
            Sv_name = itemView.findViewById(R.id.SvName);
            Sv_post = itemView.findViewById(R.id.Sv_post);
            iconLike = itemView.findViewById(R.id.iconLike);
            SoLuong = itemView.findViewById(R.id.SoLuong);
            btn_comment = itemView.findViewById(R.id.btn_comment);
            count_Comment = itemView.findViewById(R.id.count_Comment);
        }
    }

    // Hàm loadComment với callback
    public void loadComment(Context context, adapter_comment adapter, RecyclerView recyclerView, int gvPost_id, Runnable onComplete) {
        AppDatabase db = AppDatabase.getDatabase(context);
        CommentDao commentDao = db.commentDao();

        Executors.newSingleThreadExecutor().execute(() -> {
            List<Comment> comments = commentDao.getCommentsByIdPost(gvPost_id);
            new Handler(Looper.getMainLooper()).post(() -> {
                adapter.setData(comments);
                recyclerView.scrollToPosition(comments.size() - 1);
                if (onComplete != null) onComplete.run();
            });
        });
    }
}
