package com.example.caller;

public class Contact {
    private String name;
    private String phoneNumber;
    private String details;

    public Contact(String name, String phoneNumber, String details) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.details = details;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getDetails() {
        return details;
    }
}
