package com.example.myapplication.data.entity;

import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "gv_post_table",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "student_id",
                onDelete = ForeignKey.CASCADE // khi User bị xóa thì Post cũng bị xóa
        )
)
public class GvPost {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String content;
    private Uri imageUri;
    public String studentName;
    @ColumnInfo(name = "student_id")
    private int studentId;
    public GvPost() {
    }

    public GvPost(String content, Uri imageUri, int studentId,String studentName) {
        this.content = content;
        this.imageUri = imageUri;
        this.studentId = studentId;
        this.studentName = studentName;
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

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
