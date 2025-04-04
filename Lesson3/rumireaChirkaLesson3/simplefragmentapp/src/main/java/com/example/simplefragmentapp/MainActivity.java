package com.example.simplefragmentapp;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragmentContainerView) != null) {
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragmentContainerView, new FirstFragment())
                        .commit();
            }

            Button btnFirstFragment = findViewById(R.id.btnFirstFragment);
            Button btnSecondFragment = findViewById(R.id.btnSecondFragment);

            btnFirstFragment.setOnClickListener(v -> {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, new FirstFragment())
                        .commit();
            });

            btnSecondFragment.setOnClickListener(v -> {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, new SecondFragment())
                        .commit();
            });
        }
    }
}