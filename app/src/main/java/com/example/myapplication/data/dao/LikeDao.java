package com.example.myapplication.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myapplication.data.entity.Like;

import java.util.List;

@Dao
public interface LikeDao {
    @Insert
    void insert(Like like);

    @Delete
    void delete(Like like);

    @Query("SELECT * FROM like_table WHERE postId = :postId")
    List<Like> getLikesByPostId(int postId);

    @Query("SELECT * FROM like_table WHERE postId = :postId AND userId = :userId")
    Like getUserLikeForPost(int postId, int userId);
}
