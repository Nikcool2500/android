package ru.mirea.chirka.timeservice;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private TextView tvTime;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTime = findViewById(R.id.tvTime);
        Button btnFetch = findViewById(R.id.btnFetch);

        btnFetch.setOnClickListener(v -> {
            tvTime.setText("Загрузка...");
            new Thread(() -> {
                try {
                    String result = SocketManager.fetchTime();
                    String[] parts = result.split(" ");
                    String timeData = "Дата: " + parts[1] + "\nВремя: " + parts[2];
                    handler.post(() -> tvTime.setText(timeData));
                } catch (IOException e) {
                    handler.post(() -> tvTime.setText("Ошибка подключения!"));
                }
            }).start();
        });
    }
}