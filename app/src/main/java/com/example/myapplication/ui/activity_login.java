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
import com.example.myapplication.ui_admin.admin_home;

import java.util.concurrent.Executors;

public class activity_login extends AppCompatActivity {
    private EditText email, password;
    private AppCompatButton login, signup;
    private UserDao userDao;
    private String nameadmin = "Vuong", emailadmin = "admin@gmail.com", passwordadmin = "123456", mass = "", lop = "", sdt = "", role = "admin";

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

        signup.setOnClickListener(v -> startActivity(new Intent(this, activity_signup.class)));

        userDao = AppDatabase.getDatabase(getApplicationContext()).userDao();
        new Thread(() -> {
            // Kiểm tra xem admin đã tồn tại chưa
            User existingAdmin = userDao.findUserByEmail(emailadmin);
            if (existingAdmin == null) {
                User admin = new User(nameadmin, emailadmin, passwordadmin, mass, lop, sdt, role);
                userDao.insert(admin);
                runOnUiThread(() -> Toast.makeText(this, "Tạo tài khoản admin thành công", Toast.LENGTH_SHORT).show());
                startActivity(new Intent(this, admin_home.class));
            }
        }).start();

        login.setOnClickListener(v -> {
            String emailText = email.getText().toString().trim();
            String passwordText = password.getText().toString().trim();
            if (emailText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                Executors.newSingleThreadExecutor().execute(() -> {
                    User user = userDao.login(emailText, passwordText);
                    runOnUiThread(() -> {
                        if (user != null) {
                            Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(activity_login.this, activity_home.class);
                            intent.putExtra("user_id", user.getId());
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, "Email hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            }
        });
    }
}
