package ru.mirea.chirkans.mireaproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText emailField;
    private EditText passwordField;
    private ProgressBar loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        emailField = findViewById(R.id.etEmail);
        passwordField = findViewById(R.id.etPassword);
        loader = findViewById(R.id.progressBar);

        findViewById(R.id.btnLogin).setOnClickListener(v -> signIn());
        findViewById(R.id.btnRegister).setOnClickListener(v -> signUp());
    }

    private void signIn() {
        String userEmail = emailField.getText().toString().trim();
        String userPass = passwordField.getText().toString().trim();

        if (userEmail.isEmpty() || userPass.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        loader.setVisibility(View.VISIBLE);
        auth.signInWithEmailAndPassword(userEmail, userPass)
                .addOnCompleteListener(task -> {
                    loader.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Sign-in failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signUp() {
        String userEmail = emailField.getText().toString().trim();
        String userPass = passwordField.getText().toString().trim();

        if (userEmail.isEmpty() || userPass.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        loader.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(userEmail, userPass)
                .addOnCompleteListener(task -> {
                    loader.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Registration error!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().signOut();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}