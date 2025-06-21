package com.example.myapplication.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.data.entity.RegisteredActivity;

import java.util.List;

@Dao
public interface RegisteredActivityDao {
    @Insert
    void insert(RegisteredActivity activity);

    @Update
    void update(RegisteredActivity activity);

    @Delete
    void delete(RegisteredActivity activity);

    @Query("SELECT * FROM registered_activity_table WHERE userId = :userId ORDER BY startDate DESC")
    List<RegisteredActivity> getActivitiesByUserId(int userId);

    @Query("SELECT * FROM registered_activity_table WHERE postId = :postId AND userId = :userId")
    RegisteredActivity getActivity(int postId, int userId);
}
