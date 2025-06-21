package com.example.myapplication.ui_admin;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.data.AppDatabase;
import com.example.myapplication.data.dao.DocumentDao;
import com.example.myapplication.data.entity.Document;
import com.example.myapplication.data.entity.Post;

import java.util.concurrent.Executors;

public class admin_editDoc extends AppCompatActivity {
    private AppCompatButton btnTLsave, btnTLdelete;
    private EditText edClubName,edPostDescription,eduploadDoc;
    ImageView imgBack;
    private int docId;
    private DocumentDao docDao;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admin_edit_doc);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        docId = getIntent().getIntExtra("documentID", -1);
        if (docId == -1) {
            finish();
            return;
        }
        docDao = AppDatabase.getDatabase(this).documentDao();
        imgBack = findViewById(R.id.img_backcd);
        edClubName = findViewById(R.id.edClubName);
        edPostDescription = findViewById(R.id.edPostDescription);
        eduploadDoc = findViewById(R.id.eduploadDoc);
        btnTLsave = findViewById(R.id.btnTLsave);
        btnTLdelete = findViewById(R.id.btnTLdelete);
        loadUserInfo();
        imgBack.setOnClickListener(v -> finish());
        btnTLsave.setOnClickListener(v -> saveDoc());
        btnTLdelete.setOnClickListener(v -> deleteDoc());

    }
    private void loadUserInfo() {
        Executors.newSingleThreadExecutor().execute(() -> {
            Document doc = docDao.getDocById(docId);
            if (doc != null) {
                runOnUiThread(() -> {
                    edClubName.setText(doc.getTitle());
                    edPostDescription.setText(doc.getContent());
//                    eduploadDoc.setText(doc.getFileUrl());
                });
            }
        });
    }

    private void saveDoc() {
            String title = edClubName.getText().toString();
            String content = edPostDescription.getText().toString();
            String fileUrl = eduploadDoc.getText().toString();
            Document doc = new Document(title, fileUrl, content);
            doc.setId(docId);
            Executors.newSingleThreadExecutor().execute(() -> {
                docDao.update(doc);
                runOnUiThread(() -> {
                    setResult(RESULT_OK);
                });
            });
            finish();
        }

    private void deleteDoc() {
        Executors.newSingleThreadExecutor().execute(() -> {
            Document doc = docDao.getDocById(docId);
            if (doc != null) {
                docDao.delete(doc);
                runOnUiThread(() -> {
                    setResult(RESULT_OK);
                    finish();
                });
            }
        });
    }
}