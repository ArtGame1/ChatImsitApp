package com.example.chatimsitapp;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.chatimsitapp.bottomnav.chats.ChatFragment;
import com.example.chatimsitapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Log.d(TAG, "MainActivity создана");

        // ПРИ ЗАПУСКЕ НИЧЕГО НЕ ПОКАЗЫВАЕМ - ЭКРАН ПУСТОЙ

        // Настройка кнопок
        binding.bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.chats) {
                Log.d(TAG, "Нажата кнопка Chats");
                // ТОЛЬКО ЗДЕСЬ ПОКАЗЫВАЕМ ЧАТЫ
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ChatFragment())
                        .commit();
                return true;
            }
            else if (itemId == R.id.new_chat || itemId == R.id.profile) {
                // Для других кнопок просто очищаем экран
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new Fragment()) // пустой фрагмент
                        .commit();
                return true;
            }

            return false;
        });
    }
}