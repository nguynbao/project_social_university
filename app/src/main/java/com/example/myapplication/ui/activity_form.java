package com.example.myapplication.ui;

import android.content.Context;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
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
    private EditText fullnameEditText, phoneEditText, passEditText;
    TextView mssvEditText, maLopEditText,emailEditText ;
    private AppCompatButton saveButton;
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
        saveButton = findViewById(R.id.save);
        userDao =  AppDatabase.getDatabase(getApplicationContext()).userDao();
        saveButton.setOnClickListener(v -> saveUserInfo());
        loadUserInfo();
    }
    private void saveUserInfo() {
        String fullname = fullnameEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String pw = passEditText.getText().toString();
        String p = phoneEditText.getText().toString();
        if (fullname.isEmpty() || phone.isEmpty() || pw.isEmpty() || p.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
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


}