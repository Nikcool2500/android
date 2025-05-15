package ru.mirea.chirkans.mireaproject.ui.Firebase;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class UsersResponse {
    private int page;
    @SerializedName("data")
    private List<User> users;

    public int getPage() { return page; }
    public List<User> getUsers() { return users; }
}
