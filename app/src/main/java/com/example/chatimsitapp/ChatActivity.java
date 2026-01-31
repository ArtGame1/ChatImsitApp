package com.example.chatimsitapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chatimsitapp.databinding.ActivityChatBinding;
import com.example.chatimsitapp.message.Message;
import com.example.chatimsitapp.message.MessagesAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadMessages();
    }

    private void sendMessage(String message, String date) {
        HashMap<String, String> messageInfo = new HashMap<>();
        messageInfo.put("text", message);
        messageInfo.put("ownerId", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        messageInfo.put("date", date);

        FirebaseDatabase.getInstance().getReference().child("Chats");
    }

    private void loadMessages() {
        String chatId = getIntent().getStringExtra("chatId");
        if (chatId==null) return;

        FirebaseDatabase.getInstance().getReference().child("Chats")
                .child(chatId).child("messages").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) return;

                        List<Message> messages = new ArrayList<>();
                        for (DataSnapshot messageSnapshot : snapshot.getChildren()){
                            String messageId = messageSnapshot.getKey();
                            String ownerId = messageSnapshot.child("ownerId").getValue().toString();
                            String text = messageSnapshot.child("text").getValue().toString();
                            String date = messageSnapshot.child("date").getValue().toString();

                            messages.add(new Message(messageId, ownerId, text, date));
                        }
                        binding.messageRv.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                        binding.messageRv.setAdapter(new MessagesAdapter(messages));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}