package com.example.myapplication.ui_admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.example.myapplication.adapter.adapter_AllPost;
import com.example.myapplication.data.AppDatabase;
import com.example.myapplication.data.dao.PostDao;
import com.example.myapplication.data.entity.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class admin_allPost extends AppCompatActivity {
    RecyclerView recyclerViewAllPost;
    PostDao postDao;
    adapter_AllPost adapterAllPost;
    ImageView img_backBD;
    AppCompatButton create_Post;
    List<Post> postList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admin_all_post);
        recyclerViewAllPost = findViewById(R.id.recyclerViewAllPost);
        recyclerViewAllPost.setLayoutManager(new LinearLayoutManager(this));
        AppDatabase db = AppDatabase.getDatabase(this);
        PostDao postDao = db.postDao();
        Executors.newSingleThreadExecutor().execute(()->{
            List<Post> postList = postDao.getAllPosts();
            adapterAllPost = new adapter_AllPost(postList, this);
            recyclerViewAllPost.setAdapter(adapterAllPost);
        });
        create_Post = findViewById(R.id.create_Post);
        create_Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_allPost.this, admin_creatPost.class);
                startActivity(intent);
                finish();
            }
        });

        img_backBD = findViewById(R.id.img_backBD);
        img_backBD.setOnClickListener(v-> finish());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}