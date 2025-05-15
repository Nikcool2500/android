package ru.mirea.chirkans.mireaproject.ui.Firebase;

import com.google.gson.annotations.SerializedName;

public class User {
    private int id;
    private String name;
    private String email;
    private String phone;

    @SerializedName("company")
    private Company company;

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getCompany() { return company.name; }

    private static class Company {
        String name;
    }
}