package com.example.myapplication.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.data.entity.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM user_table ORDER BY id ASC")
    List<User> getAllUsers();

    @Query("SELECT * FROM user_table WHERE id = :userId")
    User getUserById(int userId);

    @Query("SELECT * FROM user_table WHERE username = :username AND password = :password")
    User login(String username, String password);
}
