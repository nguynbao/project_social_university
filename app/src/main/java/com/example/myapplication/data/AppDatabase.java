package com.example.myapplication.data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.myapplication.data.dao.NotifyDao;
import com.example.myapplication.data.dao.PostDao;
import com.example.myapplication.data.dao.RoleDao;
import com.example.myapplication.data.dao.UserDao;
import com.example.myapplication.data.entity.Notify;
import com.example.myapplication.data.entity.Post;
import com.example.myapplication.data.entity.Role;
import com.example.myapplication.data.entity.User;

import java.util.concurrent.Executors;

@Database(entities = {User.class, Role.class, Post.class, Notify.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract RoleDao roleDao();
    public abstract PostDao postDao();
    public abstract NotifyDao notifyDao();
    // Thêm các DAO khác nếu có

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .fallbackToDestructiveMigration().addCallback(new Callback() {
                                @Override
                                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                                    super.onOpen(db);
                                    Executors.newSingleThreadExecutor().execute(() -> {
                                        AppDatabase database = getDatabase(context);
                                        if (database.roleDao().count() == 0){
                                            database.roleDao().insert(new Role(1, "Admin"));
                                            database.roleDao().insert(new Role(2, "Student"));
                                            Log.d("RoomCallback", "Đã thêm role mặc định");
                                        }
                                        if (database.userDao().count() == 0) {
                                            database.userDao().insert(new User("Admin", "admin123@gmail.com", "admin123", "null", "null", "null", 1));
                                            Log.d("RoomCallback", "Đã thêm admin mặc định");
                                        }
                                    });
                                }
                            }).build();
                }
            }
        }
        return INSTANCE;
    }
}