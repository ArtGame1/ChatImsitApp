package com.example.chatimsitapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatimsitapp.chats.Chat;
import com.example.chatimsitapp.chats.ChatActivity;
import com.example.chatimsitapp.chats.ChatViewHolder;
import com.example.chatimsitapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private static final String TAG = "MainActivity";
    private RecyclerView.Adapter<ChatViewHolder> chatsAdapter;
    private List<Chat> chatList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Log.d(TAG, "MainActivity создана");

        // 1. ИНИЦИАЛИЗИРУЕМ RecyclerView со списком чатов
        setupRecyclerView();

        // 2. Показываем список чатов сразу при запуске
        binding.chatsRecyclerView.setVisibility(View.VISIBLE);

        // 3. Настройка кнопок навигации
        binding.bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.chats) {
                Log.d(TAG, "Нажата кнопка Chats");
                // Показываем список чатов
                binding.chatsRecyclerView.setVisibility(View.VISIBLE);
                return true;
            }
            else if (itemId == R.id.new_chat) {
                Log.d(TAG, "Нажата кнопка New Chat");
                // Скрываем список чатов и переходим к NewChatActivity
                binding.chatsRecyclerView.setVisibility(View.GONE);
                // Intent intent = new Intent(MainActivity.this, NewChatActivity.class);
                // startActivity(intent);
                return true;
            }
            else if (itemId == R.id.profile) {
                Log.d(TAG, "Нажата кнопка Profile");
                // Скрываем список чатов и переходим к ProfileActivity
                binding.chatsRecyclerView.setVisibility(View.GONE);
                // Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                // startActivity(intent);
                return true;
            }
            else if (itemId == R.id.exit) {
                Log.d(TAG, "Нажата кнопка Exit (выход)");

                // 1. Очищаем данные авторизации
                getSharedPreferences("UserPrefs", MODE_PRIVATE)
                        .edit()
                        .clear() // Удаляем ВСЕ сохраненные данные
                        .apply();

                Log.d(TAG, "Данные авторизации очищены");

                // 2. Переходим в LoginActivity с очисткой стека
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                // 3. Закрываем текущую активность
                finish();

                return true;
            }

            return false;
        });
    }

    private void setupRecyclerView() {
        // Создаем тестовые данные для чатов
        createTestChats();

        // Настраиваем RecyclerView
        binding.chatsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Создаем адаптер
        chatsAdapter = new RecyclerView.Adapter<ChatViewHolder>() {
            @NonNull
            @Override
            public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = getLayoutInflater().inflate(R.layout.item_chat, parent, false);
                return new ChatViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
                Chat chat = chatList.get(position);

                // Заполняем данные
                holder.chatName.setText(chat.getName());
                holder.lastMessage.setText(chat.getLastMessage());
                holder.time.setText(chat.getTime());

                // Обработка клика на чат
                holder.itemView.setOnClickListener(v -> {
                    Log.d(TAG, "Выбран чат: " + chat.getName());

                    // Переходим в ChatActivity
                    Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                    intent.putExtra("chat_id", chat.getId());
                    intent.putExtra("chat_name", chat.getName());
                    startActivity(intent);
                });
            }

            @Override
            public int getItemCount() {
                return chatList.size();
            }
        };

        binding.chatsRecyclerView.setAdapter(chatsAdapter);
    }

    private void createTestChats() {
        // Создаем тестовый список чатов
        chatList.clear();

        chatList.add(new Chat("1", "Иван Иванов", "Привет! Как дела?", "10:30"));
        chatList.add(new Chat("2", "Мария Петрова", "Жду тебя завтра в 15:00", "09:15"));
        chatList.add(new Chat("3", "Алексей Сидоров", "Документы готовы к подписанию", "Вчера"));
        chatList.add(new Chat("4", "Екатерина Васильева", "Спасибо за помощь!", "23:45"));
        chatList.add(new Chat("5", "Дмитрий Козлов", "Когда встречаемся?", "Пн"));
        chatList.add(new Chat("6", "Анна Смирнова", "Фото получила, спасибо!", "Сб"));
        chatList.add(new Chat("7", "Павел Николаев", "Завтра на работе", "Пт"));
        chatList.add(new Chat("8", "Ольга Михайлова", "Привет! Давно не виделись", "Чт"));
        chatList.add(new Chat("9", "Сергей Федоров", "Готовы ваши документы", "Ср"));
        chatList.add(new Chat("10", "Татьяна Сергеева", "Спасибо за подарок!", "25 янв"));

        Log.d(TAG, "Создано " + chatList.size() + " тестовых чатов");
    }
}