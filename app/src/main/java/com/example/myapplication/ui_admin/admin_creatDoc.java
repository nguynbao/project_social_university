package com.example.myapplication.ui_admin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.data.AppDatabase;
import com.example.myapplication.data.dao.DocumentDao;
import com.example.myapplication.data.entity.Document;

import java.util.List;
import java.util.concurrent.Executors;

public class admin_creatDoc extends AppCompatActivity {
    EditText edtPostDescription, edtClubName, uploadDoc;
    ImageView imgBack;
    Button btnUpload;

    String title, fileUrl, content;


    private static final int PICK_FILE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admin_post_document);
        edtPostDescription = findViewById(R.id.edtPostDescription);
        edtClubName = findViewById(R.id.edtClubName);
        uploadDoc = findViewById(R.id.uploadDoc);
        btnUpload = findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(v-> createDoc());
        imgBack = findViewById(R.id.img_backcd);
        imgBack.setOnClickListener(v -> finish());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void createDoc() {
        title = edtClubName.getText().toString();
        content = edtPostDescription.getText().toString();
        fileUrl = uploadDoc.getText().toString();
        Document document = new Document(title, fileUrl, content);
        Executors.newSingleThreadExecutor().execute(()->{
            AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
            DocumentDao documentDao = db.documentDao();
            documentDao.insert(document);
            List<Document> documentList = documentDao.getAllDocuments();
            for (Document document1 : documentList){
                Log.d("DocumentTest", document1.getTitle());
                Log.d("DocumentTest", document1.getContent());
                Log.d("DocumentTest", document1.getFileUrl());
            }
            runOnUiThread(()->{
                Toast.makeText(this, "Upload Tài Liệu Thành Công", Toast.LENGTH_SHORT).show();
            });
        });
        finish();
    }
}
