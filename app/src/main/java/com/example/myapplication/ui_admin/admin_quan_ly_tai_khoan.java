package com.example.myapplication.ui_admin;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
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

import java.util.concurrent.Executors;

public class admin_quan_ly_tai_khoan extends AppCompatActivity {
    private AppCompatButton btnUpload, btnDelete;
    private TextView tvUsername, tvpass, tvMssv, tvMaLop, tvrole, tvEmail;
    private ImageView imgBack;
    private int userId;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admin_thongtintaikhoan);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userId = getIntent().getIntExtra("userId", -1);
        if (userId == -1) {
            finish();
            return;
        }

        userDao = AppDatabase.getDatabase(this).userDao();
        imgBack = findViewById(R.id.img_backtk);
        tvUsername = findViewById(R.id.edtFullName);
        tvpass = findViewById(R.id.edtPass);
        tvMssv = findViewById(R.id.edtStudentId);
        tvMaLop = findViewById(R.id.edtClassId);
        tvrole = findViewById(R.id.edtRole);
        tvEmail = findViewById(R.id.edtEmail);

        btnUpload = findViewById(R.id.btnUpload);
        btnDelete = findViewById(R.id.btnDelete);

        loadUserInfo();
        imgBack.setOnClickListener(v -> finish());

        btnUpload.setOnClickListener(v -> updateUser());
        btnDelete.setOnClickListener(v -> deleteUser());
    }

    private void loadUserInfo() {
        Executors.newSingleThreadExecutor().execute(() -> {
            User user = userDao.getUserById(userId);
            UserWithRole userWithRole = userDao.getUserWithRoleById(userId);

            if (user != null) {
                runOnUiThread(() -> {
                    tvUsername.setText(user.getUsername());
                    tvpass.setText(user.getPassword());
                    tvMssv.setText(user.getMssv());
                    tvMaLop.setText(user.getMaLop());
                    tvEmail.setText(user.getEmail());
                    tvrole.setText(userWithRole.role.getRoleName());
                });
            }
        });
    }

    private void updateUser() {
        String username = tvUsername.getText().toString();
        String password = tvpass.getText().toString();
        String mssv = tvMssv.getText().toString();
        String maLop = tvMaLop.getText().toString();
        String email = tvEmail.getText().toString();
        String role = tvrole.getText().toString();
        int role_ID;
        if (role.equals("Admin")){
            role_ID =1;
        }else {
            role_ID =2;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            User updatedUser = new User();
            updatedUser.setId(userId);
            updatedUser.setUsername(username);
            updatedUser.setPassword(password);
            updatedUser.setMssv(mssv);
            updatedUser.setMaLop(maLop);
            updatedUser.setRoleId(role_ID);
            updatedUser.setEmail(email);
            userDao.update(updatedUser);
            runOnUiThread(() -> {
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            });
        });
    }

    private void deleteUser() {
        Executors.newSingleThreadExecutor().execute(() -> {
            User user = userDao.getUserById(userId);
            if (user != null) {
                userDao.delete(user);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                });
            }
        });
    }
}
