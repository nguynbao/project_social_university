package com.example.myapplication.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "document_table")
public class Document {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String fileUrl;
    private String uploader;
    private String date;

    public Document(String title, String fileUrl, String uploader, String date) {
        this.title = title;
        this.fileUrl = fileUrl;
        this.uploader = uploader;
        this.date = date;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public String getUploader() { return uploader; }
    public void setUploader(String uploader) { this.uploader = uploader; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}
