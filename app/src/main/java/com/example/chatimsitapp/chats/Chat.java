package com.example.chatimsitapp.chats;

public class Chat {
    private String id;
    private String name;
    private String lastMessage;
    private String time;

    public Chat(String id, String name, String lastMessage, String time) {
        this.id = id;
        this.name = name;
        this.lastMessage = lastMessage;
        this.time = time;
    }

    // Геттеры (ДОБАВЬТЕ ЕСЛИ ИХ НЕТ!)
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getTime() {
        return time;
    }
}