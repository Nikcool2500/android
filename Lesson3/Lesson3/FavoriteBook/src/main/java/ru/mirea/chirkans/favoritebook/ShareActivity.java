package ru.mirea.chirkans.favoritebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        TextView textViewDevBook = findViewById(R.id.textViewDevBook);
        TextView textViewDevQuote = findViewById(R.id.textViewDevQuote);
        EditText editTextUserBook = findViewById(R.id.editTextUserBook);
        EditText editTextUserQuote = findViewById(R.id.editTextUserQuote);
        Button btnSendData = findViewById(R.id.btnSendData);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String bookName = extras.getString(MainActivity.BOOK_NAME_KEY);
            String quotes = extras.getString(MainActivity.QUOTES_KEY);
            textViewDevBook.setText(bookName);
            textViewDevQuote.setText(quotes);
        }

        btnSendData.setOnClickListener(v -> {
            String userBook = editTextUserBook.getText().toString();
            String userQuote = editTextUserQuote.getText().toString();

            if (!userBook.isEmpty() && !userQuote.isEmpty()) {
                String message = "Название Вашей любимой книги: " + userBook +
                        ". Цитата: " + userQuote;

                Intent data = new Intent();
                data.putExtra(MainActivity.USER_MESSAGE, message);
                setResult(Activity.RESULT_OK, data);
                finish();
            } else {
                Toast.makeText(this, "Пожалуйста, заполните оба поля", Toast.LENGTH_SHORT).show();
            }
        });
    }
}