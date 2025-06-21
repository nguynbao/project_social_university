package com.example.myapplication.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.myapplication.data.entity.Message;

import java.util.List;

/**
 * DAO for accessing messages in the database.
 */
@Dao
public interface MessageDao {

    @Insert
    void insert(Message message);

    @Update
    void update(Message message);

    @Delete
    void delete(Message message);

    @Query("SELECT * FROM messages WHERE sender_id = :userId OR receiver_id = :userId ORDER BY timestamp ASC")
    LiveData<List<Message>> getMessagesForUser(int userId);

    @Query("SELECT * FROM messages WHERE sender_id = :senderId AND receiver_id = :receiverId ORDER BY timestamp ASC")
    LiveData<List<Message>> getConversation(int senderId, int receiverId);
}
