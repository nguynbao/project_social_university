package com.example.myapplication.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.example.myapplication.R;

import java.util.HashMap;
import java.util.Map;

public class MomoWebViewActivity extends AppCompatActivity {

    private WebView webView;
    private String momoUrl;
    private int userId;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(this);
        setContentView(webView);

        momoUrl = getIntent().getStringExtra("momoUrl");
        userId = getIntent().getIntExtra("userId", -1);
        db = FirebaseFirestore.getInstance();

        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("momo://")) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(MomoWebViewActivity.this, "Không tìm thấy ứng dụng MoMo!", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("MOMO_WEB", "Finished URL: " + url);

                // 👉 Khi redirect xong, URL trả về sẽ có dạng ?resultCode=0...
                if (url.contains("resultCode=0")) {
                    updatePaymentStatusSuccess(url);
                } else if (url.contains("resultCode")) {
                    Toast.makeText(MomoWebViewActivity.this, "Thanh toán thất bại hoặc bị hủy", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        if (momoUrl != null && (momoUrl.startsWith("http://") || momoUrl.startsWith("https://"))) {
            webView.loadUrl(momoUrl);
        } else if (momoUrl != null && momoUrl.startsWith("momo://")) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(momoUrl));
                startActivity(intent);
                finish();
            } catch (Exception e) {
                Toast.makeText(this, "Không tìm thấy ứng dụng MoMo!", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "URL không hợp lệ", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void updatePaymentStatusSuccess(String url) {
        // 👉 Tìm đơn pending hiện tại của user, update status thành success
        db.collection("Status_pay")
                .whereEqualTo("userId", userId)
                .whereEqualTo("status", "pending")
                .get()
                .addOnSuccessListener(querySnapshots -> {
                    if (!querySnapshots.isEmpty()) {
                        // Update document đầu tiên tìm được
                        querySnapshots.getDocuments().get(0).getReference()
                                .update("status", "success", "callbackUrl", url, "updatedAt", System.currentTimeMillis())
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MomoWebViewActivity.this, ThanhCongActivity.class);
                                    startActivity(intent);
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("MOMO_WEB", "Lỗi update Firestore", e);
                                    Toast.makeText(this, "Lỗi update Firestore", Toast.LENGTH_SHORT).show();
                                    finish();
                                });
                    } else {
                        // Nếu không tìm thấy pending, có thể thêm mới hoặc báo lỗi
                        Toast.makeText(this, "Không tìm thấy giao dịch cần update!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("MOMO_WEB", "Lỗi truy vấn Firestore", e);
                    Toast.makeText(this, "Lỗi truy vấn Firestore", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }
}
