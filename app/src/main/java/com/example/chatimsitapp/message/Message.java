package com.example.chatimsitapp.message;

public class Message {

    private String id, ownerId, text, date;

    public Message(String date, String id, String ownerId, String text) {
        this.date = date;
        this.id = id;
        this.ownerId = ownerId;
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
