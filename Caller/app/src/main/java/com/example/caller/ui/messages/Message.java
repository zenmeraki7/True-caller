package com.example.caller.ui.messages;

public class Message {
    private String senderName;
    private String message;
    private String time;

    public Message(String senderName, String message, String time) {
        this.senderName = senderName;
        this.message = message;
        this.time = time;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }
}
