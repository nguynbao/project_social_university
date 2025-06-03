package com.example.myapplication.ui_admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;

public class admin_home extends AppCompatActivity {
    private LinearLayout layoutCreatePost, layoutManageStudent, layoutUploadDoc, layoutCreateNotification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admin_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        layoutCreatePost = findViewById(R.id.layoutCreatePost);
        layoutManageStudent = findViewById(R.id.layoutManageStudent);
        layoutUploadDoc = findViewById(R.id.layoutUploadDoc);
        layoutCreateNotification = findViewById(R.id.layoutCreateNotification);
        layoutCreatePost.setOnClickListener(v -> {
            Toast.makeText(this, "Tạo bài đăng", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, admin_creatPost.class));
        });
        layoutManageStudent.setOnClickListener(v -> {
            Toast.makeText(this, "Quản lý sinh viên", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, admin_quan_ly_sv.class));
        });
        layoutUploadDoc.setOnClickListener(v -> {
            Toast.makeText(this, "Đăng tải tài liệu", Toast.LENGTH_SHORT).show();
        });
        layoutCreateNotification.setOnClickListener(v -> {
            Toast.makeText(this, "Tạo thông báo", Toast.LENGTH_SHORT).show();
        });


    }
}