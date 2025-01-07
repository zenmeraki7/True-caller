package com.example.caller.ui.callog;

public class CallLogEntry {
    private String name;       // Contact name
    private String number;     // Contact number
    private String callDetails; // Call type and timestamp details

    public CallLogEntry(String name, String number, String callDetails) {
        this.name = name;
        this.number = number;
        this.callDetails = callDetails;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCallDetails() {
        return callDetails;
    }

    public void setCallDetails(String callDetails) {
        this.callDetails = callDetails;
    }
}
