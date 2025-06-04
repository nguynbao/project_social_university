package com.example.myapplication.ui_admin;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.data.AppDatabase;
import com.example.myapplication.data.dao.PostDao;
import com.example.myapplication.data.entity.Post;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Executors;

public class admin_editPost extends AppCompatActivity {
    ImageView imgBack, imgUpload,icontimeSelectioned;
    TextView edClubName, edPostDescription,tvtimeSelection ;
    private int postId;
    private PostDao postDao;
    private AppCompatButton btnBDsave, btnBDdelete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admin_edit_post);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        postId = getIntent().getIntExtra("postID", -1);
        if (postId == -1) {
            finish();
            return;
        }
        postDao = AppDatabase.getDatabase(this).postDao();

        imgBack = findViewById(R.id.img_backSBD);
        imgUpload = findViewById(R.id.BDimgUpload);
        edClubName = findViewById(R.id.NameClub);
        edPostDescription = findViewById(R.id.PostDescription);
        icontimeSelectioned = findViewById(R.id.icon);
        icontimeSelectioned.setOnClickListener(v -> showDateTimePicker());
        tvtimeSelection = findViewById(R.id.tvtimeSelection);
        btnBDsave = findViewById(R.id.btnBDsave);
        btnBDdelete = findViewById(R.id.btnBDdelete);
        imgBack.setOnClickListener(v -> finish());
        btnBDsave.setOnClickListener(v -> savePost());
        btnBDdelete.setOnClickListener(v -> deletePost());
        loadPost();

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

                    TimePickerDialog timePickerDialog = new TimePickerDialog(this,
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
    private void loadPost() {
        Executors.newSingleThreadExecutor().execute(() -> {
            Post post = postDao.getPostById(postId);
            if (post != null) {
                runOnUiThread(() -> {
                    edClubName.setText(post.getNameClub());
                    edPostDescription.setText(post.getContent());
                    tvtimeSelection.setText(post.getDeadline());
//                    eduploadDoc.setText(post.getUploadDoc());
                });
            }
        });
    }
    private void savePost() {
        String nameClub = edClubName.getText().toString();
        String content = edPostDescription.getText().toString();
//        String uploadDoc = eduploadDoc.getText().toString();
        Executors.newSingleThreadExecutor().execute(() -> {
            Post updatedPost = new Post();
            updatedPost.setId(postId);
            updatedPost.setNameClub(nameClub);
            updatedPost.setContent(content);
            updatedPost.setDeadline(tvtimeSelection.getText().toString());
//            updatedPost.setUploadDoc(uploadDoc);
            postDao.update(updatedPost);
            runOnUiThread(() -> {
                setResult(RESULT_OK);
                finish();
            });
        });
    }
    private void deletePost() {
        Executors.newSingleThreadExecutor().execute(() -> {
            Post post = postDao.getPostById(postId);
            if (post != null) {
                postDao.delete(post);
                runOnUiThread(() -> {
                    setResult(RESULT_OK);
                    finish();
                });
            }
        });
        }


}