package com.emlakuygulamasi.model;

public class User {
    private String id;
    private String email;
    private String name;
    // Diğer kullanıcı alanları eklenebilir

    public User() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
} 