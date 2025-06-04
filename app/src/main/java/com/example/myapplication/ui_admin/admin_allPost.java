package com.example.myapplication.ui_admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.adapter_bai_dang;
import com.example.myapplication.adapter.adapter_sinh_vien;
import com.example.myapplication.data.AppDatabase;
import com.example.myapplication.data.dao.PostDao;
import com.example.myapplication.data.entity.Post;
import com.example.myapplication.data.entity.User;

import java.util.ArrayList;
import java.util.List;

public class admin_allPost extends AppCompatActivity {
    RecyclerView recyclerView;
    adapter_bai_dang adapter;
    PostDao postDao;

    ImageView back;
    AppCompatButton addBD;
    List<Post> postList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admin_all_post);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerViewBD);
        postDao = AppDatabase.getDatabase(this).postDao();
        back = findViewById(R.id.img_backBD);
        back.setOnClickListener(v -> {
            finish();
    });
        loadData();
        addBD = findViewById(R.id.addBD);
        addBD.setOnClickListener(v -> {
            startActivity(new Intent(this, admin_creatPost.class));
        });

}
    private void loadData() {
        // Dùng LiveData để quan sát dữ liệu
        postDao.getAllPostsByliveData().observe(this, posts -> {
            postList = posts; // Cập nhật danh sách

            // Thiết lập RecyclerView
            if (adapter == null) {
                adapter = new adapter_bai_dang(posts);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(adapter);
            } else {
                adapter.setData(posts);
            }

        });
    }
}