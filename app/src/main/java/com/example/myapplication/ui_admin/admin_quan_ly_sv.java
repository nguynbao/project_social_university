package com.example.myapplication.ui_admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
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
    AutoCompleteTextView search;
    AppCompatButton addSV;
    UserDao userDao;
    ImageView back;
    List<User> fullUserList = new ArrayList<>();

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
        search = findViewById(R.id.search_sv);
        userDao = AppDatabase.getDatabase(this).userDao();
        back = findViewById(R.id.img_back);
        back.setOnClickListener(v -> {
            finish();
        });
        loadData();
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                searchUser(query);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
        addSV = findViewById(R.id.addSV);
        addSV.setOnClickListener(v -> {
            startActivity(new Intent(this, admin_them_sv.class));
        });
    }
    private void loadData() {
        // Kiểm tra và thêm dữ liệu mẫu
        Executors.newSingleThreadExecutor().execute(() -> {
            // Đoạn kiểm tra dữ liệu mẫu vẫn chạy nền
            boolean check = true;
            if (userDao.count() == 0) { // Dùng count() thay cho getAllUsers().isEmpty()
                userDao.insert(new User("Nguyen Van A", "a@gmail.com", "123456", "111111", "123456", "123456", 1));
                userDao.insert(new User("Tran Thi B", "b@gmail.com", "123456", "222222", "123456", "123456", 2));
                userDao.insert(new User("Le Van C", "c@gmail.com", "123456", "333333", "123456", "123456", 1));
                userDao.insert(new User("Pham Thi D", "d@gmail.com", "123456", "444444", "123456", "123456", 2));
                userDao.insert(new User("Hoang Van E", "e@gmail.com", "123456", "555555", "123456", "123456", 1));
            }
        });

        // Dùng LiveData để quan sát dữ liệu
        userDao.getAllUsers().observe(this, users -> {
            fullUserList = users; // Cập nhật danh sách

            // Thiết lập RecyclerView
            if (adapter == null) {
                adapter = new adapter_sinh_vien(users);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(adapter);
            } else {
                adapter.setData(users);
            }

            // Thiết lập gợi ý AutoCompleteTextView
            List<String> userMssvList = new ArrayList<>();
            for (User user : users) {
                userMssvList.add(user.getMssv());
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_dropdown_item_1line,
                    userMssvList
            );
            search.setAdapter(arrayAdapter);
        });
    }

    private void searchUser(String query) {
        LiveData<List<User>> filteredUsersLiveData;
        if (query.isEmpty()) {
            filteredUsersLiveData = userDao.getAllUsers();
        } else {
            filteredUsersLiveData = userDao.searchUsers(query);
        }

        filteredUsersLiveData.observe(this, users -> {
            adapter.setData(users);
        });
    }
}