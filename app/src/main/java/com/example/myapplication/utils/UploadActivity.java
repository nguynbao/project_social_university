package com.example.myapplication.utils;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class UploadActivity extends AppCompatActivity {
    private static final String ACCESS_KEY = "key";
    private static final String SECRET_KEY = "key";
    private static final String BUCKET_NAME = "baonguynbucket";

    private TransferUtility transferUtility;
    private ImageView imageView, loadImg;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        imageView = findViewById(R.id.imageView);
        loadImg = findViewById(R.id.loadimg);
        Button uploadButton = findViewById(R.id.uploadButton);

        // Init AWS client
        BasicAWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        AmazonS3Client s3Client = new AmazonS3Client(credentials);
        s3Client.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_2));  // Sydney
        transferUtility = TransferUtility.builder()
                .s3Client(s3Client)
                .context(getApplicationContext())
                .build();

        imageView.setOnClickListener(v -> openGallery());

        uploadButton.setOnClickListener(v -> {
            if (selectedImageUri != null) {
                File file = createFileFromUri(selectedImageUri);
                if (file != null) {
                    uploadFile(file);
                } else {
                    Toast.makeText(this, "Không đọc được file ảnh!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Chọn ảnh trước!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imageView.setImageURI(selectedImageUri);
        }
    }

    private File createFileFromUri(Uri uri) {
        try {
            String fileName = getFileName(uri);
            File file = new File(getCacheDir(), fileName);
            InputStream inputStream = getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);

            byte[] buf = new byte[4096];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getFileName(Uri uri) {
        String result = "upload_" + System.currentTimeMillis() + ".jpg";
        try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (index >= 0) {
                    result = cursor.getString(index);
                }
            }
        }
        return result;
    }

    private void uploadFile(File file) {
        String keyInS3 = "uploads/" + file.getName();
        transferUtility.upload(BUCKET_NAME, keyInS3, file)
                .setTransferListener(new TransferListener() {
                    @Override
                    public void onStateChanged(int id, TransferState state) {
                        if (state == TransferState.COMPLETED) {
                            Toast.makeText(UploadActivity.this, "Upload thành công!", Toast.LENGTH_SHORT).show();
                            String imageUrl = "https://" + BUCKET_NAME + ".s3.ap-southeast-2.amazonaws.com/" + keyInS3;
                            Glide.with(UploadActivity.this)
                                    .load(imageUrl)
                                    .into(loadImg);  // 👉 Load vào loadimg sau khi upload thành công
                            Log.d("UploadActivity", "Image URL: " + imageUrl);
                        } else if (state == TransferState.FAILED) {
                            Toast.makeText(UploadActivity.this, "Upload thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                        // Optional: Bạn có thể thêm progress bar ở đây
                    }
                    @Override
                    public void onError(int id, Exception ex) {
                        ex.printStackTrace();
                        Toast.makeText(UploadActivity.this, "Lỗi: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

}
