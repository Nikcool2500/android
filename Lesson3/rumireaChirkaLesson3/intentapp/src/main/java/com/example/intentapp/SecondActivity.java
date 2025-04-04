package com.example.intentapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        TextView tvTime = findViewById(R.id.tvTime);
        String time = getIntent().getStringExtra("time");

        int groupNumber = 26;
        int square = groupNumber * groupNumber;
        String message = "КВАДРАТ ЗНАЧЕНИЯ МОЕГО НОМЕРА ПО СПИСКУ В ГРУППЕ СОСТАВЛЯЕТ " + square + ", а текущее время " + time;

        tvTime.setText(message);
    }
}