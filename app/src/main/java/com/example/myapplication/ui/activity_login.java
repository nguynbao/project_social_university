package com.example.myapplication.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.util.List;
import java.util.concurrent.Executors;

public class activity_login extends AppCompatActivity {
    private EditText email, password;
    private AppCompatButton login;
    private UserDao userDao;
    User user ;



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
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getEmail = email.getText().toString();
                String getPass = password.getText().toString();
                Executors.newSingleThreadExecutor().execute(()-> {
                    AppDatabase db =  AppDatabase.getDatabase(getApplicationContext());
                    userDao = db.userDao();
                    List<UserWithRole> userWithRoleList = userDao.getAllUsersWithRole();
                    boolean loginSuccess = false;
                    for (UserWithRole userWithRole :userWithRoleList){
                        if (userWithRole.user.getEmail().equals(getEmail) && userWithRole.user.getPassword().equals(getPass)){
                            loginSuccess = true;
                            user = userWithRole.user;
                            int roleId = userWithRole.user.getRoleId();
                            runOnUiThread(()->{
                                if (roleId == 1){
                                    Intent intent = new Intent(activity_login.this, admin_home.class);
                                    startActivity(intent);
                                }else {
                                    SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putInt("student_id", user.getId());
                                    editor.apply();
                                    Intent intent = new Intent(activity_login.this, activity_home.class);
                                    startActivity(intent);
                                }
                            });
                            break;
                        }
                    }
                    if (!loginSuccess){
                        runOnUiThread(() -> {
                            Toast.makeText(activity_login.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            }
        });
    }
}