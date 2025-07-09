package com.example.saferoute.models;

public class User {
    private String id;
    private String username;
    private String password;
    private String contactNumber;
    private String role; // "admin" or "user"

    public User() {
    }

    public User(String id, String username, String password, String contactNumber, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.contactNumber = contactNumber;
        this.role = role;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
