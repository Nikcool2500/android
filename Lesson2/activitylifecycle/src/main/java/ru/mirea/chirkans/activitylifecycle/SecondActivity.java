package ru.mirea.chirkans.activitylifecycle;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.PersistableBundle;
import android.util.Log;
import android.widget.TextView;



public class SecondActivity extends AppCompatActivity {
    /*
        Ответы на вопросы:
        1. Метод onCreate не будет вызван
        2. Значение EditText не изменится
        3. Значение EditText изменится
    */
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        textView = findViewById(R.id.textView2);

        String text = getIntent().getStringExtra("key");
        textView.setText(text);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart2");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume2");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause2");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop2");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy2");
    }
}