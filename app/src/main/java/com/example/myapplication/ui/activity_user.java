package com.example.myapplication.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.bumptech.glide.Glide;
import com.example.myapplication.BuildConfig;
import com.example.myapplication.R;
import com.example.myapplication.adapter.adapter_detail_user;
import com.example.myapplication.data.AppDatabase;
import com.example.myapplication.data.dao.UserDao;
import com.example.myapplication.data.entity.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class activity_user extends AppCompatActivity {

    private static final String BUCKET_NAME = "baonguynbucket";

    private RecyclerView recyclerView;
    private ImageView back, Avatar;
    private AppCompatButton save;
    private TextView name, lop;
    private UserDao userDao;
    private Uri selectedImageUri;
    private adapter_detail_user adapterDetailUser;
    private List<String> listImgUrls = new ArrayList<>();
    private int userId;
    private int currentUploadPosition = -1; // -1: avatar, >=0: item position

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedPreferences = this.getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("student_id", -1);
        userDao = AppDatabase.getDatabase(getApplicationContext()).userDao();

        recyclerView = findViewById(R.id.recyclerView);
        back = findViewById(R.id.back);
        Avatar = findViewById(R.id.imageView3);
        save = findViewById(R.id.appCompatButton);
        name = findViewById(R.id.name);
        lop = findViewById(R.id.lop);

        Executors.newSingleThreadExecutor().execute(() -> {
            User user = userDao.getUserById(userId);
            runOnUiThread(() -> {
                if (user != null) {
                    name.setText(user.getUsername());
                    lop.setText(user.getMaLop());
                    Toast.makeText(this, "Chào " + user.getUsername(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        Avatar.setOnClickListener(v -> {
            currentUploadPosition = -1;
            openGallery();
        });

        back.setOnClickListener(v -> finish());

        save.setOnClickListener(v -> {
            if (selectedImageUri == null) {
                Toast.makeText(this, "Vui lòng chọn ảnh trước khi lưu!", Toast.LENGTH_SHORT).show();
                return;
            }
            File file = createFileFromUri(selectedImageUri);
            if (file != null) {
                uploadAndShow(file, "uploads/avatar/user_" + userId + ".jpg", -1);
            } else {
                Toast.makeText(this, "Không đọc được file ảnh!", Toast.LENGTH_SHORT).show();
            }
        });

        for (int i = 0; i < 6; i++) {
            listImgUrls.add("https://" + BUCKET_NAME + ".s3.ap-southeast-2.amazonaws.com/uploads/picdetail/user_"
                    + userId + "_img" + i + ".jpg?v=" + System.currentTimeMillis());
        }

        adapterDetailUser = new adapter_detail_user(listImgUrls, this, true, position -> {
            currentUploadPosition = position;
            openGallery();
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapterDetailUser);

        String avatarUrl = "https://" + BUCKET_NAME + ".s3.ap-southeast-2.amazonaws.com/uploads/avatar/user_"
                + userId + ".jpg?v=" + System.currentTimeMillis();
        Glide.with(this).load(avatarUrl).placeholder(R.drawable.img_1).into(Avatar);
    }

    private void uploadAndShow(File file, String keyInS3, int positionToUpdate) {
        String accessKey = BuildConfig.AWS_ACCESS_KEY;
        String secretKey = BuildConfig.AWS_SECRET_KEY;
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3Client s3Client = new AmazonS3Client(credentials);
        s3Client.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_2));

        TransferUtility transferUtility = TransferUtility.builder()
                .context(getApplicationContext())
                .s3Client(s3Client)
                .build();

        transferUtility.upload(BUCKET_NAME, keyInS3, file)
                .setTransferListener(new TransferListener() {
                    @Override
                    public void onStateChanged(int id, TransferState state) {
                        if (state == TransferState.COMPLETED) {
                            String imageUrl = "https://" + BUCKET_NAME + ".s3.ap-southeast-2.amazonaws.com/" + keyInS3 + "?v=" + System.currentTimeMillis();
                            runOnUiThread(() -> {
                                Toast.makeText(activity_user.this, "Upload thành công!", Toast.LENGTH_SHORT).show();
                                if (positionToUpdate == -1) {
                                    Glide.with(activity_user.this).load(imageUrl).into(Avatar);
                                } else {
                                    listImgUrls.set(positionToUpdate, imageUrl);
                                    adapterDetailUser.notifyItemChanged(positionToUpdate);
                                }
                            });
                        } else if (state == TransferState.FAILED) {
                            runOnUiThread(() -> Toast.makeText(activity_user.this, "Upload thất bại!", Toast.LENGTH_SHORT).show());
                        }
                    }

                    @Override
                    public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {}

                    @Override
                    public void onError(int id, Exception ex) {
                        ex.printStackTrace();
                        runOnUiThread(() -> Toast.makeText(activity_user.this, "Lỗi: " + ex.getMessage(), Toast.LENGTH_LONG).show());
                    }
                });
    }

    private File createFileFromUri(Uri uri) {
        try {
            String fileName = "img_" + System.currentTimeMillis() + ".jpg";
            File file = new File(getCacheDir(), fileName);
            InputStream inputStream = getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[4096];
            int len;
            while ((len = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.close();
            inputStream.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                File file = createFileFromUri(selectedImageUri);
                if (file != null) {
                    if (currentUploadPosition == -1) {
                        uploadAndShow(file, "uploads/avatar/user_" + userId + ".jpg", -1);
                    } else {
                        uploadAndShow(file, "uploads/picdetail/user_" + userId + "_img" + currentUploadPosition + ".jpg", currentUploadPosition);
                    }
                }
            }
        }
    }
}
