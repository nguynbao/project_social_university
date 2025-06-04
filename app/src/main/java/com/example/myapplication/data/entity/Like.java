package com.example.myapplication.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "like_table")
public class Like {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int postId;
    private int userId;


    public Like(int postId, int userId) {
        this.postId = postId;
        this.userId = userId;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
}
