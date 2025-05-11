package ru.mirea.chirkans.mireaproject.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ru.mirea.chirkans.mireaproject.R;

public class ProfileFragment extends Fragment {

    private SharedPreferences userPreferences;
    private static final String PREFS_NAME = "user_settings";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        userPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        EditText nicknameField = rootView.findViewById(R.id.nameField);
        EditText experienceField = rootView.findViewById(R.id.numberField);
        EditText specializationField = rootView.findViewById(R.id.filmField);
        Button updateProfileBtn = rootView.findViewById(R.id.updateProfileBtn);

        nicknameField.setText(userPreferences.getString("USER_NAME", ""));
        experienceField.setText(String.valueOf(userPreferences.getInt("NUMBER", 0)));
        specializationField.setText(userPreferences.getString("FAVOURITE_FILM", ""));

        updateProfileBtn.setOnClickListener(v -> {
            try {
                SharedPreferences.Editor editor = userPreferences.edit();
                editor.putString("USER_NAME", nicknameField.getText().toString());
                editor.putInt("NUMBER", Integer.parseInt(experienceField.getText().toString()));
                editor.putString("FAVOURITE_FILM", specializationField.getText().toString());
                editor.apply();

                Toast.makeText(getContext(), "Настройки обновлены", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Проверьте введенные данные", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}