package com.example.myapplication.ui_admin;

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

import com.example.myapplication.R;
import com.example.myapplication.data.AppDatabase;
import com.example.myapplication.data.dao.RoleDao;
import com.example.myapplication.data.dao.UserDao;
import com.example.myapplication.data.entity.Role;
import com.example.myapplication.data.entity.User;

import java.util.concurrent.Executors;

public class admin_quan_ly_tai_khoan extends AppCompatActivity {
    private AppCompatButton btnUpload, btnDelete;
    private TextView tvUsername, tvpass, tvMssv, tvMaLop, tvrole;

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
        int userId = getIntent().getIntExtra("userId", -1);
        if (userId == -1) {
            // Xử lý khi không nhận được id
            finish();
            return;
        }
        UserDao userDao = AppDatabase.getDatabase(this).userDao();
        Executors.newSingleThreadExecutor().execute(() -> {
            User user = userDao.getUserById(userId);
            RoleDao roleDao = AppDatabase.getDatabase(this).roleDao();

            if (user != null) {
                Role role = roleDao.getRoleById(user.roleId());
                runOnUiThread(() -> {
                    // Hiển thị dữ liệu lên UI
                   tvUsername = findViewById(R.id.etFullName);
                   tvMssv = findViewById(R.id.etStudentId);
                   tvMaLop = findViewById(R.id.etClassId);
                   tvpass = findViewById(R.id.edtpass);
                   tvrole = findViewById(R.id.edtRole);

                    tvUsername.setText(user.getUsername());
                    tvpass.setText(user.getPassword());
                    tvMssv.setText(user.getMssv());
                    tvMaLop.setText(user.getMaLop());
                   if (role != null) {
                       tvrole.setText(role.getRoleName());
                   }else {
                       tvrole.setText("Không tìm thấy vai trò");
                   }
                });
            }
        });
        btnUpload = findViewById(R.id.btnUpload);
        btnDelete = findViewById(R.id.btnDelete);
//        btnUpload.setOnClickListener(v -> {
//            String username = tvUsername.getText().toString();
//            String password = tvpass.getText().toString();
//            String mssv = tvMssv.getText().toString();
//            String maLop = tvMaLop.getText().toString();
//            int roleint = 2;
//            String role = tvrole.getText().toString();Executors.newSingleThreadExecutor().execute(() -> {
//                User updatedUser = new User();
//                updatedUser.setId(userId); // giữ nguyên ID cũ
//                updatedUser.setUsername(username);
//                updatedUser.setPassword(password);
//                updatedUser.setMssv(mssv);
//                updatedUser.setMaLop(maLop);
//                updatedUser.setRole(roleint);
//
//                userDao.update(updatedUser);
//                runOnUiThread(() -> {
//                    // Optionally hiển thị Toast hoặc quay lại màn hình trước
//                    Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
//                    finish();
//                });
//            });
//        });

    }}