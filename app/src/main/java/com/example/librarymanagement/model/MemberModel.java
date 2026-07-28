package com.example.librarymanagement.model;

public class MemberModel {
    private final int id;
    private final String name;
    private final String email;
    private final String phone;
    private final String address;

    public MemberModel(int id, String name, String email, String phone, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
}
