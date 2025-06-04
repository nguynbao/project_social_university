package com.example.myapplication.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.data.AppDatabase;
import com.example.myapplication.data.dao.GvPostDao;
import com.example.myapplication.data.dao.UserDao;
import com.example.myapplication.data.entity.GvPost;

import java.util.concurrent.Executors;

public class activity_post_gr_sv extends AppCompatActivity {
    ImageView GV_img_back, GV_imgUpload;
    EditText GV_post;
    AppCompatButton GV_btnUpload;
    GvPostDao gvPostDao;
    Uri selectedImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_post_gr_sv);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        int studentId = sharedPreferences.getInt("student_id", -1);

        gvPostDao = AppDatabase.getDatabase(this).gvPostDao();
        GV_img_back = findViewById(R.id.GV_img_back);
        GV_img_back.setOnClickListener(v -> finish());

        GV_imgUpload = findViewById(R.id.GV_imgUpload);
        GV_imgUpload.setOnClickListener(v -> openGallery());

        GV_post = findViewById(R.id.GV_post);

        GV_btnUpload = findViewById(R.id.GV_btnUpload);
        GV_btnUpload.setOnClickListener(v -> {
            String content = GV_post.getText().toString();
            Uri imageUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.img_bai_dang);

            Executors.newSingleThreadExecutor().execute(() -> {
                UserDao userDao = AppDatabase.getDatabase(this).userDao();
                String studentName = userDao.getUserNameById(studentId);

                GvPost post = new GvPost(content, imageUri, studentId, studentName);
                gvPostDao.insert(post);

                runOnUiThread(this::finish);
            });
        });

    }
    public void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 100);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            GV_imgUpload.setImageURI(selectedImageUri);
            // Lưu selectedImageUri để sử dụng khi upload (tuỳ bạn)
        }
    }
}
