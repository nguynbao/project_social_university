package com.example.myapplication.data.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "post_table")
public class Post {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nameClub;
    private String content;
    private String deadline;
    private String imagePath;
    private String urlJoin;

    public Post(String nameClub, String content, String deadline,String imagePath,String urlJoin) {
        this.nameClub = nameClub;
        this.content = content;
        this.deadline = deadline;
        this.imagePath = imagePath;
        this.urlJoin = urlJoin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameClub() {
        return nameClub;
    }

    public void setNameClub(String nameClub) {
        this.nameClub = nameClub;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getUrlJoin() {
        return urlJoin;
    }

    public void setUrlJoin(String urlJoin) {
        this.urlJoin = urlJoin;
    }
}
