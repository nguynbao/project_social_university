package com.example.myapplication.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class activity_update_account extends AppCompatActivity {
    private FirebaseFirestore db;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // ✅ Gọi setContentView luôn ngay đầu
        setContentView(R.layout.activity_update_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Lấy userId từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("student_id", -1);

        db = FirebaseFirestore.getInstance();

        // Check Firestore sau khi layout đã hiển thị
        checkStatusFromFirestore();

        AppCompatButton update = findViewById(R.id.update);
        ImageView back = findViewById(R.id.back);

        back.setOnClickListener(v -> {
            finish();
        });

        update.setOnClickListener(v -> {
            Intent intent = new Intent(activity_update_account.this, acitvity_pay.class);
            startActivity(intent);
        });
    }

    private void checkStatusFromFirestore() {
        db.collection("Status_pay")
                .whereEqualTo("userId", userId)
                .whereEqualTo("status", "success")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Nếu đã thanh toán, chuyển luôn
                        Intent intent = new Intent(activity_update_account.this, ThanhCongActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("FIREBASE", "Lỗi khi check Firestore", e);
                    Toast.makeText(this, "Lỗi khi kiểm tra trạng thái", Toast.LENGTH_SHORT).show();
                });
    }
}
