package com.example.myapplication.ui_admin;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.concurrent.Executors;

public class admin_them_sv extends AppCompatActivity {
    EditText etFullName, etEmail, etStudentId, edtpass, etClassId, edphone, edtRole;
    AppCompatButton btnSave;
    ImageView imgBack;
    UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admin_them_sv);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        imgBack = findViewById(R.id.img_backtsv);
        imgBack.setOnClickListener(v -> finish());
        userDao = AppDatabase.getDatabase(this).userDao();
        etFullName = findViewById(R.id.edtFullNamead);
        etEmail = findViewById(R.id.edtEmailad);
        etStudentId = findViewById(R.id.edtStudentIdad);
        edtpass = findViewById(R.id.edtPassad);
        etClassId = findViewById(R.id.edtClassIdad);
        edphone = findViewById(R.id.edtPhonead);
        edtRole = findViewById(R.id.edtRolead);
        btnSave = findViewById(R.id.btnSavead);
        btnSave.setOnClickListener(v -> {
            int role_ID;
            String fullName = etFullName.getText().toString();
            String email = etEmail.getText().toString();
            String studentId = etStudentId.getText().toString();
            String pass = edtpass.getText().toString();
            String classId = etClassId.getText().toString();
            String phone = edphone.getText().toString();
            String role = edtRole.getText().toString();
            if (role.equals("Admin")||role.equals("admin")){
                role_ID =1;

            }else {
                role_ID =2;
            }
            Executors.newSingleThreadExecutor().execute(() -> {
                userDao.insert(new User(fullName, email, pass, studentId, classId, phone, role_ID));
                runOnUiThread(() -> {
                    Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    finish();
                });
            });
        });



    }
}