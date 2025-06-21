package com.example.myapplication.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "comment_table")
public class Comment {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String content;
    private int GvPost_Id;

    public Comment( String content, int GvPost_Id) {
        this.content = content;
        this.GvPost_Id  = GvPost_Id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getGvPost_Id() {
        return GvPost_Id;
    }

    public void setGvPost_Id(int gvPost_Id) {
        GvPost_Id = gvPost_Id;
    }
}
