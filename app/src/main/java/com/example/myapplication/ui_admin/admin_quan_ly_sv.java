package com.example.myapplication.ui_admin;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.adapter_sinh_vien;
import com.example.myapplication.data.dao.UserDao;
import com.example.myapplication.data.entity.User;
import com.example.myapplication.data.AppDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class admin_quan_ly_sv extends AppCompatActivity {
    RecyclerView recyclerView;
    adapter_sinh_vien adapter;
    List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admin_quanllysinhvien);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerView = findViewById(R.id.recyclerViewSV1);



        Executors.newSingleThreadExecutor().execute(() -> {
            UserDao userDao = AppDatabase.getDatabase(this).userDao();
            // Thêm dữ liệu mẫu nếu database trống
            if (userDao.getAllUsers().isEmpty()) {
                userDao.insert(new User("Nguyen Van A", "a@gmail.com", "123456", "123456", "123456", "123456", 1));
                userDao.insert(new User("Tran Thi B", "b@gmail.com", "123456", "123456", "123456", "123456", 2));
                userDao.insert(new User("Le Van C", "c@gmail.com", "123456", "123456", "123456", "123456", 1));
                userDao.insert(new User("Pham Thi D", "d@gmail.com", "123456", "123456", "123456", "123456", 2));
                userDao.insert(new User("Hoang Van E", "e@gmail.com", "123456", "123456", "123456", "123456", 1));
            }

            // Lấy danh sách user từ database
            List<User> userList = userDao.getAllUsers();

            runOnUiThread(() -> {
                adapter = new adapter_sinh_vien(userList);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(adapter);
            });
        });
    }}