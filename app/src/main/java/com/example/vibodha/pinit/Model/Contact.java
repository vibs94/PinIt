package com.example.vibodha.pinit.Model;

/**
 * Created by vibodha on 3/26/17.
 */

public class Contact {

    private String name;
    private String phoneNumber;
    private String message;

    public Contact(String name, String phoneNumber, String message) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.message = message;
        ;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getMessage() {
        return message;
    }
}
