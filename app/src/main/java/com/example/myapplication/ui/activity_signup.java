package com.example.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

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

import java.util.concurrent.Executors;

public class activity_signup extends AppCompatActivity {
    private EditText emailEditText, passEditText;
    private AppCompatButton btnNext;
    UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_dang_ky);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        emailEditText = findViewById(R.id.email);
        passEditText = findViewById(R.id.pass);
        btnNext = findViewById(R.id.btnNext);
        userDao =  AppDatabase.getDatabase(getApplicationContext()).userDao();
        btnNext.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String pass = passEditText.getText().toString();

            Executors.newSingleThreadExecutor().execute(() -> {
                userDao.insert(new User(email, pass));

                // Quay về Main Thread để mở activity
                runOnUiThread(() -> {
                    Intent intent = new Intent(activity_signup.this, activity_form.class);
                    intent.putExtra("email", email);
                    intent.putExtra("password", pass);
                    startActivity(intent);
                });
            });
        });
    }
}