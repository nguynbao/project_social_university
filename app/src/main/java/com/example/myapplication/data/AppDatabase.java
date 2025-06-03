package com.example.myapplication.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.myapplication.data.dao.RoleDao;
import com.example.myapplication.data.dao.UserDao;
import com.example.myapplication.data.entity.Role;
import com.example.myapplication.data.entity.User;

import java.util.concurrent.Executors;

@Database(entities = {User.class, Role.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract RoleDao roleDao();
    // Thêm các DAO khác nếu có

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .fallbackToDestructiveMigration().addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Executors.newSingleThreadExecutor().execute(() -> {
                                        getDatabase(context).roleDao().insert(new Role(1, "Admin"));
                                        getDatabase(context).roleDao().insert(new Role(2, "User"));
                                    });
                                }
                            }).build();
                }
            }
        }
        return INSTANCE;
    }
}
