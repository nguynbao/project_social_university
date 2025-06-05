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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
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

public class admin_editPost extends AppCompatActivity {
    ImageView imgBack, img_Upload,icontimeSelectioned;
    EditText urlLink;
    TextView edClubName, edPostDescription,tvtimeSelection ;
    private int postId;
    private PostDao postDao;
    private AppCompatButton btnBDsave, btnBDdelete;
    String imagePath;
    int REQUEST_CODE_IMAGE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admin_edit_post);
        urlLink = findViewById(R.id.urlLink);
        edClubName = findViewById(R.id.NameClub);
        edPostDescription = findViewById(R.id.PostDescription);
        tvtimeSelection = findViewById(R.id.tvtimeSelection);


        btnBDsave = findViewById(R.id.btnBDsave);
        Intent intent = getIntent();
        postId = intent.getIntExtra("id_Post", -1);
        loadPost(postId);

        img_Upload = findViewById(R.id.img_Upload);
        img_Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mở thư viện ảnh để chọn
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_IMAGE);
            }
        });

        icontimeSelectioned = findViewById(R.id.icontimeSelectioned);
        icontimeSelectioned.setOnClickListener(v -> showDateTimePicker());

        btnBDdelete = findViewById(R.id.btnBDdelete);
        btnBDdelete.setOnClickListener(v -> deletePost());

        btnBDsave.setOnClickListener(v -> savePost());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void savePost() {
        if(imagePath!= null){
            String nameClub = edClubName.getText().toString();
            String content = edPostDescription.getText().toString();
            String dealine = tvtimeSelection.getText().toString();
            String urlJoint = urlLink.getText().toString();
            if(!nameClub.isEmpty() && !content.isEmpty() && !dealine.isEmpty() && urlJoint != null){
                if(Patterns.WEB_URL.matcher(urlJoint).matches()){
                    Post post = new Post(nameClub, content, dealine, imagePath, urlJoint);
                    postDao = AppDatabase.getDatabase(getApplicationContext()).postDao();
                    Executors.newSingleThreadExecutor().execute(()->{
                        Post post1 = postDao.getPostById(postId);
                        postDao.delete(post1);
                        postDao.insert(post);
                        runOnUiThread(()->{
                            Toast.makeText(admin_editPost.this, "Chỉnh bài thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(admin_editPost.this, admin_allPost.class);
                            startActivity(intent);
                            finish();
                        });
                    });
                }else {
                    Toast.makeText(this, "Vui lòng nhập đúng Link Website", Toast.LENGTH_SHORT).show();
                }

            }else{
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin ", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(admin_editPost.this, "Vui lòng thêm ảnh hoặc nhập đủ thông tin", Toast.LENGTH_SHORT).show();
        }

    }

    public void loadPost(int postId){
        postDao = AppDatabase.getDatabase(getApplicationContext()).postDao();
        Executors.newSingleThreadExecutor().execute(()->{
            Post post = postDao.getPostById(postId); // ví dụ
            runOnUiThread(() -> {
                if (edClubName != null && post != null) {
                    edClubName.setText(post.getNameClub());
                    edPostDescription.setText(post.getContent());
                    tvtimeSelection.setText(post.getDeadline());
                    Glide.with(admin_editPost.this).load(new File(post.getImagePath())).into(img_Upload);
                    urlLink.setText(post.getUrlJoin());
                } else {
                    Log.e("DEBUG", "tvPostName hoặc post bị null");
                    Log.e("DEBUG", post.getNameClub());
                }
            });
        });
    }
    public void deletePost(){
        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        postDao = db.postDao();
        Executors.newSingleThreadExecutor().execute(()->{
            Post post = postDao.getPostById(postId);
            postDao.delete(post);
        });
        Intent intent1 = new Intent(admin_editPost.this, admin_allPost.class);
        startActivity(intent1);
        finish();
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

                    TimePickerDialog timePickerDialog = new TimePickerDialog(admin_editPost.this,
                            (timeView, hourOfDay, minute) -> {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);

                                // Hiển thị ra EditText
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                                tvtimeSelection.setText(sdf.format(calendar.getTime()));
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
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                img_Upload.setImageURI(selectedImageUri); // hiển thị ảnh vừa chọn
                // Tạo tên file
                String fileName = "img_" + System.currentTimeMillis() + ".jpg";

                // Copy ảnh về bộ nhớ trong
                String localPath = copyFileToInternalStorage(selectedImageUri, fileName);
                if (localPath != null) {
                    imagePath = localPath; // Gán vào biến để sau này lưu vào Room
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