package com.example.myapplication.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "registered_activity_table")
public class RegisteredActivity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int userId;
    private int postId;
    private String startDate;
    private String endDate;
    private String proofImagePath; // đường dẫn ảnh minh chứng

    public RegisteredActivity(int userId, int postId, String startDate, String endDate, String proofImagePath) {
        this.userId = userId;
        this.postId = postId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.proofImagePath = proofImagePath;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getProofImagePath() { return proofImagePath; }
    public void setProofImagePath(String proofImagePath) { this.proofImagePath = proofImagePath; }
}
