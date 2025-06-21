package com.example.myapplication.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.myapplication.data.entity.User;
import com.example.myapplication.data.entity.UserWithRole;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM user_table ORDER BY id ASC")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * FROM user_table WHERE id = :userId")
    User getUserById(int userId);

    @Query("SELECT * FROM user_table WHERE email = :email AND password = :password")
    User login(String email, String password);
    @Query("SELECT * FROM user_table WHERE email = :email LIMIT 1")
    User findUserByEmail(String email);

    @Transaction
    @Query("SELECT * FROM user_table")
    List<UserWithRole> getAllUsersWithRole();

    @Transaction
    @Query("SELECT * FROM user_table WHERE id = :userId")
    UserWithRole getUserWithRoleById(int userId);

    @Query("SELECT COUNT(*) FROM user_table")
    int count();
    @Query("SELECT * FROM user_table " +
            "WHERE mssv LIKE '%' || :query || '%' " +
            "OR username LIKE '%' || :query || '%' " +
            "OR email LIKE '%' || :query || '%'")
    LiveData<List<User>> searchUsers(String query);
    @Query("SELECT username FROM user_table WHERE id = :studentId")
    String getUserNameById(int studentId);

}
