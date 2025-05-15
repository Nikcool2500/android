package ru.mirea.chirkans.mireaproject.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mirea.chirkans.mireaproject.R;
import ru.mirea.chirkans.mireaproject.ui.Firebase.ApiClient;
import ru.mirea.chirkans.mireaproject.ui.Firebase.User;
import ru.mirea.chirkans.mireaproject.ui.Firebase.UsersResponse;

public class FirebaseFragment extends Fragment {

    private ProgressBar progressBar;
    private TextView textViewData;

    public FirebaseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_firebase, container, false);

        progressBar = root.findViewById(R.id.progressBar);
        textViewData = root.findViewById(R.id.tvData);

        loadUsersData();

        return root;
    }
    private void loadUsersData() {
        progressBar.setVisibility(View.VISIBLE);
        ApiClient.getService().getUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    displayUsers(response.body());
                } else {
                    showError("Ошибка: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                showError("Ошибка сети: " + t.getMessage());
            }
        });
    }

    private void displayUsers(List<User> users) {
        StringBuilder sb = new StringBuilder();
        for (User user : users) {
            sb.append("ID: ").append(user.getId())
                    .append("\nИмя: ").append(user.getName())
                    .append("\nEmail: ").append(user.getEmail())
                    .append("\nТелефон: ").append(user.getPhone())
                    .append("\nКомпания: ").append(user.getCompany())
                    .append("\n\n");
        }
        textViewData.setText(sb.toString());
    }

    private void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        textViewData.setText(message);
    }
}