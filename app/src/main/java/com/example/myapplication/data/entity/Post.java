package com.example.myapplication.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "post_table")
public class Post {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String content;
    private String author; // tên người đăng (hoặc id nếu cần join)
    private String date;
    private boolean isActivity; // true nếu là hoạt động, false nếu là bài viết thường
    private String deadline; // hạn đăng ký (chỉ cần thiết với hoạt động)

    // Constructor
    public Post(String title, String content, String author, String date, boolean isActivity, String deadline) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.date = date;
        this.isActivity = isActivity;
        this.deadline = deadline;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public boolean isActivity() { return isActivity; }
    public void setActivity(boolean activity) { isActivity = activity; }

    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }
}
