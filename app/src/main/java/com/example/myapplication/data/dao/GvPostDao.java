package com.example.myapplication.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myapplication.data.entity.GvPost;

import java.util.List;
@Dao
public interface GvPostDao {
    @Insert
    void insert(GvPost post);

    @Query("SELECT * FROM gv_post_table ORDER BY id DESC")
    LiveData<List<GvPost>> getAllPostsLiveData();


}
