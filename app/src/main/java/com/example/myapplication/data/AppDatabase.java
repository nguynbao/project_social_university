package com.example.myapplication.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myapplication.data.dao.RegisteredActivityDao;
import com.example.myapplication.data.dao.UserDao;
import com.example.myapplication.data.entity.RegisteredActivity;
import com.example.myapplication.data.entity.User;
import com.example.myapplication.data.dao.CommentDao;
import com.example.myapplication.data.entity.Comment;
import com.example.myapplication.data.dao.PostDao;
import com.example.myapplication.data.entity.Post;
import com.example.myapplication.data.dao.DocumentDao;
import com.example.myapplication.data.entity.Document;
import com.example.myapplication.data.dao.LikeDao;
import com.example.myapplication.data.entity.Like;

@Database(entities = {User.class, Comment.class, Post.class, Document.class, Like.class, RegisteredActivity.class},  version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract CommentDao commentDao();
    public abstract PostDao postDao();
    public abstract DocumentDao documentDao();
    public abstract LikeDao likeDao();
    public abstract RegisteredActivityDao registeredActivityDao();


    // Thêm các DAO khác nếu có

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
