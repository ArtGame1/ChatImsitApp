package com.example.chatimsitapp.bottomnav.chats;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chatimsitapp.chats.Chat;
import com.example.chatimsitapp.chats.ChatsAdapter;
import com.example.chatimsitapp.databinding.FragmentChatsBinding;

import java.util.ArrayList;

public class ChatFragment extends Fragment {
    private FragmentChatsBinding binding;
    private ChatsAdapter adapter;
    private static final String TAG = "ChatFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChatsBinding.inflate(inflater, container, false);
        Log.d(TAG, "ChatFragment onCreateView");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "ChatFragment onViewCreated");

        //Сразу загружаем чаты при создании фрагмента
        loadChats();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "ChatFragment onResume");
        //При возвращении на фрагмент обновляем данные
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void loadChats() {
        try {
            Log.d(TAG, "Загрузка тестовых чатов...");

            //Создаем тестовые чаты
            ArrayList<Chat> chats = new ArrayList<>();
            chats.add(new Chat("123", "Алексей Петров", "user1", "current_user"));
            chats.add(new Chat("124", "Мария Иванова", "user2", "current_user"));
            chats.add(new Chat("125", "Иван Сидоров", "user3", "current_user"));
            chats.add(new Chat("126", "Ольга Смирнова", "user4", "current_user"));
            chats.add(new Chat("127", "Дмитрий Козлов", "user5", "current_user"));
            chats.add(new Chat("128", "Екатерина Волкова", "user6", "current_user"));
            chats.add(new Chat("129", "Сергей Морозов", "user7", "current_user"));

            Log.d(TAG, "Создано " + chats.size() + " тестовых чатов");

            //Настройка RecyclerView
            binding.chatsRv.setLayoutManager(new LinearLayoutManager(getContext()));

            //Создаем адаптер
            adapter = new ChatsAdapter(chats);
            binding.chatsRv.setAdapter(adapter);

            //Добавляем разделитель между элементами
            //binding.chatsRv.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

            Log.d(TAG, "Адаптер установлен успешно");

            //Проверяем видимость
            if (binding.chatsRv.getVisibility() != View.VISIBLE) {
                binding.chatsRv.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            Log.e(TAG, "Ошибка при загрузке чатов: " + e.getMessage(), e);
        }
    }
}