package com.example.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.adapter.adapter_detail_user;
import com.example.myapplication.data.AppDatabase;
import com.example.myapplication.data.dao.UserDao;
import com.example.myapplication.data.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class activity_orther extends AppCompatActivity {
    TextView name, age, lop;
    ImageView back, imageView3;
    AppCompatButton appCompatButton;
    RecyclerView recyclerView;
    adapter_detail_user adapter;
    List<String> listImgUrls = new ArrayList<>();
    User user;
    UserDao userDao;
    int studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_orther);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        userDao = AppDatabase.getDatabase(this).userDao();
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        lop = findViewById(R.id.lop);
        back = findViewById(R.id.back);
        imageView3 = findViewById(R.id.imageView3);
        appCompatButton = findViewById(R.id.appCompatButton11);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        studentId = getIntent().getIntExtra("studentId", -1);
        if (studentId != -1) {
            Executors.newSingleThreadExecutor().execute(() -> {
                user = userDao.getUserById(studentId);
                runOnUiThread(() -> {
                    if (user != null) {
                        name.setText(user.getUsername());
                        lop.setText(user.getMaLop());

                        // Load avatar
                        String avatarUrl = "https://baonguynbucket.s3.ap-southeast-2.amazonaws.com/uploads/avatar/user_"
                                + studentId + ".jpg?v=" + System.currentTimeMillis();
                        Glide.with(this)
                                .load(avatarUrl)
                                .placeholder(R.drawable.img_1)
                                .into(imageView3);

                        // Tạo danh sách 6 hình
                        listImgUrls.clear();
                        for (int i = 0; i < 6; i++) {
                            String url = "https://baonguynbucket.s3.ap-southeast-2.amazonaws.com/uploads/picdetail/user_"
                                    + studentId + "_img" + i + ".jpg?v=" + System.currentTimeMillis();
                            listImgUrls.add(url);
                        }

                        // Gắn adapter
                        adapter = new adapter_detail_user(listImgUrls, this, false, null);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(this, "Không tìm thấy người dùng!", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        } else {
            Toast.makeText(this, "Không có studentId hợp lệ!", Toast.LENGTH_SHORT).show();
        }

        back.setOnClickListener(v -> finish());

        appCompatButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, activity_message.class);
            intent.putExtra("receiverId", studentId);
            startActivity(intent);
        });
    }
}
