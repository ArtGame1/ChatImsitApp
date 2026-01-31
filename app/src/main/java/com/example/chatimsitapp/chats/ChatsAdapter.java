package com.example.chatimsitapp.chats;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatimsitapp.ChatActivity;
import com.example.chatimsitapp.R;

import java.util.ArrayList;

public class ChatsAdapter extends RecyclerView.Adapter<ChatViewHolder> {

    private ArrayList<Chat> chats;

    public ChatsAdapter(ArrayList<Chat> chats) {
        this.chats = chats;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.person_item_rv, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = chats.get(position);

        // Устанавливаем имя чата
        if (holder.chat_name_tv != null) {
            holder.chat_name_tv.setText(chat.getChat_name());
        }

        // Устанавливаем дефолтную иконку
        if (holder.chat_iv != null) {
            // Можно использовать любую иконку из ресурсов
            // holder.chat_iv.setImageResource(R.drawable.baseline_person_24);
        }

        // Обработчик клика
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), ChatActivity.class);
            intent.putExtra("chatId", chats.get(position).getChat_id());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return chats != null ? chats.size() : 0;
    }
}