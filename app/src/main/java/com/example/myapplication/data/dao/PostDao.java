package com.example.myapplication.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.data.entity.Post;

import java.util.List;

@Dao
public interface PostDao {
    @Insert
    void insert(Post post);

    @Update
    void update(Post post);

    @Delete
    void delete(Post post);

    @Query("SELECT * FROM post_table ORDER BY deadline DESC")
    List<Post> getAllPosts();
    @Query("SELECT * FROM post_table ORDER BY deadline DESC")
    LiveData<List<Post>> getAllPostsByliveData();

    @Query("SELECT * FROM post_table WHERE id = :postId")
    Post getPostById(int postId);
}
