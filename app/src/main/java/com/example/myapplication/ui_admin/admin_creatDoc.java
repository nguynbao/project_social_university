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
    EditText edtPostDescription, edtClubName;
    ImageView uploadDoc;
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
        uploadDoc.setOnClickListener(v -> uploadDocFunction());
        btnUpload = findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(v-> createDoc());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void uploadDocFunction() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Chọn tệp tài liệu"), PICK_FILE_REQUEST);
    }

    private void createDoc() {
        title = edtClubName.getText().toString();
        content = edtPostDescription.getText().toString();
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null){
            Uri selectedFileUri = data.getData();
            if(selectedFileUri != null){
                Toast.makeText(this, "Upload thành công" + String.valueOf(selectedFileUri), Toast.LENGTH_SHORT).show();
                fileUrl = String.valueOf(selectedFileUri);
            }
        }
    }
}
