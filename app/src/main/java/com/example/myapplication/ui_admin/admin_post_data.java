package com.example.myapplication.ui_admin;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.content.pm.PackageManager;
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
import com.example.myapplication.data.dao.NotifyDao;
import com.example.myapplication.data.entity.Notify;

import java.util.concurrent.Executors;

public class admin_post_data extends AppCompatActivity {
    private AppCompatButton btnUpload;
    private EditText content, title;
    ImageView back;
    NotifyDao notifyDao;

    private static final int REQUEST_CODE_POST_NOTIFICATIONS = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admin_post_data);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        back = findViewById(R.id.img_backpd);
        back.setOnClickListener(v -> {
            finish();
        });
        notifyDao = AppDatabase.getDatabase(this).notifyDao();
        btnUpload = findViewById(R.id.btnUpload);
        content = findViewById(R.id.content);
        title = findViewById(R.id.title);

        // Tạo Notification Channel 1 lần duy nhất
        createNotificationChannel();

        btnUpload.setOnClickListener(v -> {
            String contentText = content.getText().toString();
            String titleText = title.getText().toString();
            if (contentText.isEmpty() || titleText.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            // Kiểm tra permission trước khi thao tác
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_POST_NOTIFICATIONS);
                    return; // dừng tại đây chờ cấp quyền
                }
            }

            // Thực hiện insert và hiển thị notification
            Executors.newSingleThreadExecutor().execute(() -> {
                notifyDao.insert(new Notify(titleText, contentText));
                runOnUiThread(() -> {
                    showNotification(titleText, contentText);
                    Toast.makeText(this, "Đăng tải thành công", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                });
            });
        });
    }

    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String channelId = "upload_channel";
            String channelName = "Upload Status";
            String channelDescription = "Thông báo khi upload thành công";
            int importance = android.app.NotificationManager.IMPORTANCE_DEFAULT;

            android.app.NotificationChannel channel = new android.app.NotificationChannel(channelId, channelName, importance);
            channel.setDescription(channelDescription);

            android.app.NotificationManager notificationManager = getSystemService(android.app.NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_POST_NOTIFICATIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Đã cấp quyền thông báo", Toast.LENGTH_SHORT).show();
                // Bạn có thể gọi showNotification ở đây nếu muốn tự động hiển thị
                // Nhưng phải lưu tạm data title/content nếu muốn dùng
            } else {
                Toast.makeText(this, "Bạn cần cấp quyền để hiển thị thông báo", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showNotification(String title, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "upload_channel")
                .setSmallIcon(android.R.drawable.stat_sys_upload_done)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // Lại kiểm tra permission để an toàn
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        notificationManager.notify(1, builder.build());
    }
}
