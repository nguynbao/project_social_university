package com.example.myapplication.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "document_table")
public class Document {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String fileUrl;
    private String content;

    public Document(String title, String fileUrl, String content ) {
        this.title = title;
        this.fileUrl = fileUrl;
        this.content = content;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public String getContent() { return content; }
    public void setContent(String uploader) { this.content = content; }
}
