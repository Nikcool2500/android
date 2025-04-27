package ru.mirea.chirka.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private EditText ageInput;
    private EditText occupationInput;
    private Button sendButton;

    // Handler для приёма ответов из MyLooper
    private Handler mainHandler = new Handler(msg -> {
        Bundle data = msg.getData();
        String result = data.getString("result");
        // Выводим результат в лог
        Log.d(TAG, result);
        return true;
    });

    private MyLooper myLooper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ageInput = findViewById(R.id.ageInput);
        occupationInput = findViewById(R.id.occupationInput);
        sendButton = findViewById(R.id.sendButton);

        // Запускаем поток с собственным Looper
        myLooper = new MyLooper(mainHandler);
        myLooper.start();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ageStr = ageInput.getText().toString().trim();
                String occupation = occupationInput.getText().toString().trim();
                if (ageStr.isEmpty() || occupation.isEmpty()) {
                    Log.d(TAG, "Пожалуйста, заполните оба поля");
                    return;
                }

                // Формируем сообщение и отправляем в MyLooper
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("age", ageStr);
                bundle.putString("occupation", occupation);
                msg.setData(bundle);
                // Убедимся, что handler потока создан
                while (myLooper.mHandler == null) {
                    // ждём инициализации
                }
                myLooper.mHandler.sendMessage(msg);
            }
        });
    }
}
