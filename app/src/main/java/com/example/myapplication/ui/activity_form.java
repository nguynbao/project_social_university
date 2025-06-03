package com.example.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.data.AppDatabase;
import com.example.myapplication.data.dao.UserDao;
import com.example.myapplication.data.entity.User;

public class activity_form extends AppCompatActivity {
    private EditText fullnameEditText, mssvEditText, maLopEditText, phoneEditText;
    private AppCompatButton saveButton;
    UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_dien_thong_tin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        fullnameEditText = findViewById(R.id.fullname);
        mssvEditText = findViewById(R.id.Mssv);
        maLopEditText = findViewById(R.id.ma_lop);
        phoneEditText = findViewById(R.id.phone);
        saveButton = findViewById(R.id.save);
        userDao =  AppDatabase.getDatabase(getApplicationContext()).userDao();
    }
}