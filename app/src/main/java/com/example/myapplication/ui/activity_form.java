package com.example.myapplication.ui;

import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.Executors;
import com.example.myapplication.R;
import com.example.myapplication.data.AppDatabase;
import com.example.myapplication.data.dao.UserDao;
import com.example.myapplication.data.entity.User;

public class activity_form extends AppCompatActivity {
    private EditText  phoneEditText, passEditText;
    TextView mssvEditText, maLopEditText,emailEditText,fullnameEditText ;
    private AppCompatButton saveButton;
    ImageView back, Avatar;
    Uri selectedImageUri;
    UserDao userDao;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        SharedPreferences sharedPreferences = this.getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("student_id", -1);
        setContentView(R.layout.user_dien_thong_tin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        fullnameEditText = findViewById(R.id.fullname);
        mssvEditText = findViewById(R.id.MSSV);
        maLopEditText = findViewById(R.id.ma_lop);
        phoneEditText = findViewById(R.id.phone);
        passEditText = findViewById(R.id.pass);
        emailEditText = findViewById(R.id.Email);
        Avatar = findViewById(R.id.imageView2);
        Avatar.setOnClickListener(v -> {
            openGallery();
        });
        saveButton = findViewById(R.id.save);
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            finish();
        });
        userDao =  AppDatabase.getDatabase(getApplicationContext()).userDao();
        saveButton.setOnClickListener(v -> saveUserInfo());
        loadUserInfo();
    }
//    private void saveUserInfo() {
//        String pw = passEditText.getText().toString();
//        String p = phoneEditText.getText().toString();
//        if (pw.isEmpty() || p.isEmpty()) {
//            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        User user = new User();
//        user.setPassword(pw);
//        user.setPhone(p);
//        user.setId(userId);
//        Executors.newSingleThreadExecutor().execute(() -> {
//            userDao.update(user);
//            runOnUiThread(() -> {
//                Toast.makeText(this, "Thông tin đã được cập nhật", Toast.LENGTH_SHORT).show();
//            });
//            finish();
//        });
//    }
private void saveUserInfo() {
    String pw = passEditText.getText().toString();
    String p = phoneEditText.getText().toString();
    if (pw.isEmpty() || p.isEmpty()) {
        Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        return;
    }
    Executors.newSingleThreadExecutor().execute(() -> {
        User user = userDao.getUserById(userId);
        if (user != null) {
            user.setPassword(pw);
            user.setPhone(p);
            userDao.update(user);
            runOnUiThread(() -> {
                Toast.makeText(this, "Thông tin đã được cập nhật", Toast.LENGTH_SHORT).show();
            });
            finish();
        } else {
            runOnUiThread(() -> {
                Toast.makeText(this, "Không tìm thấy người dùng!", Toast.LENGTH_SHORT).show();
            });
        }
    });
}

    private void loadUserInfo() {
        Executors.newSingleThreadExecutor().execute(() -> {
            User user = userDao.getUserById(userId);

            if (user != null) {
                runOnUiThread(() -> {
                    fullnameEditText.setText(user.getUsername());
                    mssvEditText.setText(user.getMssv());
                    maLopEditText.setText(user.getMaLop());
                    emailEditText.setText(user.getEmail());
                    passEditText.setText(user.getPassword());
                    phoneEditText.setText(user.getPhone());
                });
            }
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
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                Avatar.setImageURI(selectedImageUri);
                // Tạo tên file
                String fileName = "img_" + System.currentTimeMillis() + ".jpg";
                // Copy ảnh về bộ nhớ trong
//                String localPath = copyFileToInternalStorage(selectedImageUri, fileName);
//                if (localPath != null) {
//                    imageFilePath = localPath; // Gán vào biến để sau này lưu vào Room
//                    Toast.makeText(this, "Đã lưu ảnh vào: " + localPath, Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(this, "Lỗi khi sao chép ảnh", Toast.LENGTH_SHORT).show();
//                }
            }

        }
    }

}