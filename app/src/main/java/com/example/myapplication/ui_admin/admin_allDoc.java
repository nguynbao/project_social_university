package com.example.myapplication.ui_admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.adapter_tai_lieu;
import com.example.myapplication.data.AppDatabase;
import com.example.myapplication.data.dao.DocumentDao;
import com.example.myapplication.data.entity.Document;

import java.util.ArrayList;
import java.util.List;

public class admin_allDoc extends AppCompatActivity {
    RecyclerView recyclerView;
    adapter_tai_lieu adapter;
    DocumentDao documentDao;
    List<Document> documentList = new ArrayList<>();
    AppCompatButton addTL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admin_all_doc);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerView = findViewById(R.id.recyclerViewTL);
        documentDao = AppDatabase.getDatabase(this).documentDao();
        addTL = findViewById(R.id.addTL);

        loadData();
        addTL.setOnClickListener(v -> {
            startActivity(new Intent(this, admin_creatDoc.class));
        });

    }
    private void loadData() {
        documentDao.getAllDocumentsbyliveData().observe(this, documents -> {
            documentList = documents; // Cập nhật danh sách
            if (adapter == null) {
                adapter = new adapter_tai_lieu(documents);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.setData(documents);
            }
        });
    }
}