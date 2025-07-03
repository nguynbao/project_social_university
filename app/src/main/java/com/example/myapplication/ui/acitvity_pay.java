package com.example.myapplication.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import com.example.myapplication.data.AppDatabase;
import com.example.myapplication.data.dao.UserDao;
import com.google.firebase.firestore.FirebaseFirestore;

public class acitvity_pay extends AppCompatActivity {
    ImageView back;
    AppCompatButton btn_pay, btn_momo;
    private int userId;
    private UserDao userDao;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_acitvity_pay);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedPreferences = this.getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("student_id", -1);
        userDao = AppDatabase.getDatabase(getApplicationContext()).userDao();
        db = FirebaseFirestore.getInstance();

        back = findViewById(R.id.back);
        btn_pay = findViewById(R.id.vnpay);
        btn_momo = findViewById(R.id.momo);

        back.setOnClickListener(v -> finish());

        btn_momo.setOnClickListener(v -> {
            @SuppressLint("StaticFieldLeak")
            MomoPaymentTask momoTask = new MomoPaymentTask(this, userId);
            momoTask.execute();
        });

        // 👉 Xử lý khi app được mở từ scheme (lần đầu hoặc mở lại)
        handleMomoResult(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 👉 Xử lý khi đang chạy và được gọi lại qua scheme
        handleMomoResult(intent);
    }

    private void handleMomoResult(Intent intent) {
        Uri data = intent.getData();
        if (data != null && "myapp".equals(data.getScheme())) {
            String resultCode = data.getQueryParameter("resultCode");
            String message = data.getQueryParameter("message");
            String orderId = data.getQueryParameter("orderId");

            Log.d("MOMO_RESULT", "resultCode: " + resultCode + ", message: " + message);

            if ("0".equals(resultCode)) {
                updateFirestoreSuccess(orderId, data.toString());
            } else {
                Toast.makeText(this, "Thanh toán thất bại hoặc bị hủy", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateFirestoreSuccess(String orderId, String callbackUrl) {
        db.collection("Status_pay")
                .whereEqualTo("userId", userId)
                .whereEqualTo("status", "pending")
                .get()
                .addOnSuccessListener(querySnapshots -> {
                    if (!querySnapshots.isEmpty()) {
                        querySnapshots.getDocuments().get(0).getReference()
                                .update("status", "success", "callbackUrl", callbackUrl, "orderId", orderId, "updatedAt", System.currentTimeMillis())
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(acitvity_pay.this, ThanhCongActivity.class));
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("PAYMENT", "Lỗi update Firestore", e);
                                    Toast.makeText(this, "Lỗi update Firestore", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(this, "Không tìm thấy giao dịch pending", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("PAYMENT", "Lỗi truy vấn Firestore", e);
                    Toast.makeText(this, "Lỗi truy vấn Firestore", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        db.collection("Status_pay")
                .whereEqualTo("userId", userId)
                .whereEqualTo("status", "success")
                .get()
                .addOnSuccessListener(querySnapshots -> {
                    if (!querySnapshots.isEmpty()) {
                        Intent intent = new Intent(acitvity_pay.this, ThanhCongActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("PAYMENT", "Lỗi khi kiểm tra Firestore", e);
                });
    }
}
