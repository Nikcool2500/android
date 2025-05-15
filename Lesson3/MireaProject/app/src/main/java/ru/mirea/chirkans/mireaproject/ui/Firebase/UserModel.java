package ru.mirea.chirkans.mireaproject.ui.Firebase;

import com.google.gson.annotations.SerializedName;

public class UserModel {
    private int id;
    private String email;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    public int getId() { return id; }
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (" + email + ")";
    }
}
