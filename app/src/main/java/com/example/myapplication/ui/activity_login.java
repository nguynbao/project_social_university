package com.example.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.myapplication.data.entity.UserWithRole;
import com.example.myapplication.ui_admin.admin_home;

import java.util.concurrent.Executors;

public class activity_login extends AppCompatActivity {
    private EditText email, password;
    private AppCompatButton login, signup;
    private UserDao userDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_trang_dang_nhap);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        email = findViewById(R.id.email);
        password = findViewById(R.id.pass);
        login = findViewById(R.id.btn_login);
        signup = findViewById(R.id.btn_signup);
        AppDatabase db =  AppDatabase.getDatabase(getApplicationContext());

        userDao = db.userDao();

        User user = new User("Ngô Thành Danh", "tinhoc7645@gmail.com", "787870", "225481028", "22DHTT06", "0919212029",1);
        Executors.newSingleThreadExecutor().execute(()-> {
            userDao.insert(user);
        });

        Executors.newSingleThreadExecutor().execute(() ->{
            UserWithRole userWithRole = userDao.getUserWithRoleById(1);
            if (userWithRole != null){
                Log.d("User:", userWithRole.user.getUsername());
                Log.d("Quyền:", userWithRole.role.getRoleName());
            }
        });

    }
}
