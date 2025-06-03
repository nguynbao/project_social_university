package com.example.myapplication.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "comment_table")
public class Comment {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int postId;
    private int userId;
    private String content;
    private String date;

    // Constructor
    public Comment(int postId, int userId, String content, String date) {
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.date = date;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}
