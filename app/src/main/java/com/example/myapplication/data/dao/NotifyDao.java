package com.example.myapplication.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myapplication.data.entity.Notify;

import java.util.List;
@Dao
public interface NotifyDao {
    @Insert
    void insert(Notify notify);
    @Delete
    void delete(Notify notify);
    @Query("SELECT * FROM notify_table")
    LiveData<List<Notify>> getAllNotifies();

}
