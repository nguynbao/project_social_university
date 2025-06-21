package com.example.myapplication.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.adapter_message;
import com.example.myapplication.data.entity.Message;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class activity_message extends AppCompatActivity {
    private RecyclerView recyclerView;
    private adapter_message adapter;
    private List<Message> messages;
    ImageButton sendButton;
    ImageView back;
    EditText inputMessage;
    private DatabaseReference chatRef;

    private int senderId;
    private int receiverId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Lấy dữ liệu từ Intent
        SharedPreferences sharedPreferences = this.getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        senderId = sharedPreferences.getInt("student_id", -1);
        receiverId = getIntent().getIntExtra("receiverId", -1);
        sendButton = findViewById(R.id.imageButton);
        inputMessage = findViewById(R.id.input_mess);
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            finish();
        });
        // Kiểm tra hợp lệ
        if (senderId == -1 || receiverId == -1) {
            finish();
            return;
        }
        // Khởi tạo messages
        messages = new ArrayList<>();
        // Thiết lập RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new adapter_message(this, messages, senderId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        // Tạo chatId
        String chatId = generateChatId(senderId, receiverId);
        chatRef = FirebaseDatabase.getInstance().getReference("chats").child(chatId);
        loadMessages();
        sendButton.setOnClickListener(v -> {
            String messageText = inputMessage.getText().toString().trim();
            if (!messageText.isEmpty()) {
                Message message = new Message(senderId, receiverId, messageText, true);
                chatRef.child("messages").push().setValue(message);
                inputMessage.setText("");
            }
        });
    }
    private void loadMessages() {
        chatRef.child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Message message = data.getValue(Message.class);
                    if (message != null) {
                        messages.add(message);
                    }
                }
                // Cập nhật RecyclerView
                adapter.notifyDataSetChanged();
                // Cuộn xuống tin nhắn mới nhất
                recyclerView.scrollToPosition(messages.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý error nếu cần
            }
        });
    }

    private String generateChatId(int user1, int user2) {
        return user1 < user2 ? user1 + "_" + user2 : user2 + "_" + user1;
    }
}
