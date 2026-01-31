package com.example.chatimsitapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatimsitapp.databinding.ActivityChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
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



    private void loadMessages() {

    }
}