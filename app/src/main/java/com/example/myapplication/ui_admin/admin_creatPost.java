package com.example.myapplication.ui_admin;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.data.AppDatabase;
import com.example.myapplication.data.dao.PostDao;
import com.example.myapplication.data.entity.Post;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class admin_creatPost extends AppCompatActivity {
    private static final  int REQUEST_CODE_IMAGE =1;
    private Uri selectedImageUri = null;
    EditText edtNameClub, etPostDescription, urlLink;
    TextView edttimeSelection;
    Button btnUpload;
    ImageView timeSelection, imgUpload, imgBack;
    String imagePath_Uri, nameClub, content, deadline, urlJoin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admin_create_post);
        urlLink = findViewById(R.id.urlLink);
        edtNameClub = findViewById(R.id.edtNameClub);
        etPostDescription = findViewById(R.id.etPostDescription);
        edttimeSelection = findViewById(R.id.edttimeSelection);
        timeSelection = findViewById(R.id.timeSelection);
        imgUpload = findViewById(R.id.imgUpload);
        btnUpload = findViewById(R.id.btnUpload);
        timeSelection.setOnClickListener(v -> showDateTimePicker());
        imgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mở thư viện ảnh để chọn
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_IMAGE);
            }
        });
        imgBack = findViewById(R.id.img_backcp);
        imgBack.setOnClickListener(v -> finish());

        btnUpload.setOnClickListener(v-> createPost());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    private void createPost() {
        if (imagePath_Uri != null){
            nameClub = edtNameClub.getText().toString();
            content = etPostDescription.getText().toString();
            deadline = edttimeSelection.getText().toString();
            urlJoin = urlLink.getText().toString();
            if(!nameClub.isEmpty() && !content.isEmpty() && !deadline.isEmpty() && urlJoin != null) {
                if (Patterns.WEB_URL.matcher(urlJoin).matches()) {
                    Post post = new Post(nameClub, content, deadline, imagePath_Uri, urlJoin);
                    Executors.newSingleThreadExecutor().execute(() -> {
                        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
                        PostDao postDao = db.postDao();
                        postDao.insert(post);
                        runOnUiThread(() -> {
                            Toast.makeText(admin_creatPost.this, "Đăng bài thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(admin_creatPost.this, admin_allPost.class);
                            startActivity(intent);
                            finish();
                        });
                    });
                }else {
                    Toast.makeText(this, "Vui lòng nhập đúng Link Website", Toast.LENGTH_SHORT).show();
                }
            }else {
                    Toast.makeText(this, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
        }else {
            Toast.makeText(admin_creatPost.this, "Vui lòng thêm ảnh", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDateTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        Log.d("Test", "đã gọi được vào đây");
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    // Sau khi chọn ngày xong, mở TimePicker
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(admin_creatPost.this,
                            (timeView, hourOfDay, minute) -> {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);

                                // Hiển thị ra EditText
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                                edttimeSelection.setText(sdf.format(calendar.getTime()));
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true);

                    timePickerDialog.show();

                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    // Hàm nhận kết quả chọn ảnh
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                imgUpload.setImageURI(selectedImageUri); // hiển thị ảnh vừa chọn
                // Tạo tên file
                String fileName = "img_" + System.currentTimeMillis() + ".jpg";

                // Copy ảnh về bộ nhớ trong
                String localPath = copyFileToInternalStorage(selectedImageUri, fileName);
                if (localPath != null) {
                    imagePath_Uri = localPath; // Gán vào biến để sau này lưu vào Room
                    Toast.makeText(this, "Đã lưu ảnh vào: " + localPath, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Lỗi khi sao chép ảnh", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String copyFileToInternalStorage(Uri uri, String fileName) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream == null) return null;

            File file = new File(getFilesDir(), fileName);
            FileOutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            return file.getAbsolutePath();  // Trả về đường dẫn kiểu: /data/data/your.package.name/files/xxx.jpg
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
