package com.example.chatimsitapp.chats;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatimsitapp.R;

public class ChatViewHolder extends RecyclerView.ViewHolder {
    public ImageView avatar;
    public TextView chatName;
    public TextView lastMessage;
    public TextView time;

    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);

        // Находим элементы по ID из item_chat.xml
        avatar = itemView.findViewById(R.id.avatar);
        chatName = itemView.findViewById(R.id.chat_name);
        lastMessage = itemView.findViewById(R.id.last_message);
        time = itemView.findViewById(R.id.time);

        // Проверка на null (как в вашем исходном коде)
        if (avatar == null) {
            android.util.Log.e("ChatViewHolder", "avatar не найден!");
        }
        if (chatName == null) {
            android.util.Log.e("ChatViewHolder", "chatName не найден!");
        }
        if (lastMessage == null) {
            android.util.Log.e("ChatViewHolder", "lastMessage не найден!");
        }
        if (time == null) {
            android.util.Log.e("ChatViewHolder", "time не найден!");
        }
    }
}