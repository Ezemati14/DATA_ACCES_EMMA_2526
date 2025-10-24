package org.emma2025.unit1;

import java.io.Serializable;

public class Contact implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String surName;
    private String email;
    private String phoneNumber;
    private String description;

    public Contact(String name, String surName, String phoneNumber, String email, String description) {
        this.name = name;
        this.description = description;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.surName = surName;
    }

    // Getters
    public String getFullName() {
        return name + " " + surName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return "Name: " + name + " " + surName + "\nEmail: " + email + "\nPhone: " + phoneNumber + "\nDescription: " + description;
    }
}
