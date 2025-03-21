package ru.mirea.chirkans.dialog;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button timeButton = findViewById(R.id.timeButton);
        timeButton.setOnClickListener(v -> {
            MyTimeDialogFragment timeDialog = new MyTimeDialogFragment();
            timeDialog.show(getSupportFragmentManager(), "timePicker");
        });

        Button dateButton = findViewById(R.id.dateButton);
        dateButton.setOnClickListener(v -> {
            MyDateDialogFragment dateDialog = new MyDateDialogFragment();
            dateDialog.show(getSupportFragmentManager(), "datePicker");
        });

        Button progressButton = findViewById(R.id.progressButton);
        progressButton.setOnClickListener(v -> {
            MyProgressDialogFragment progressDialog = new MyProgressDialogFragment();
            progressDialog.show(getSupportFragmentManager(), "progressDialog");
        });
    }
    public void onClickShowDialog(View view) {
        MyDialogFragment dialogFragment = new MyDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "mirea");
    }

    public void onOkClicked() {
        Toast.makeText(getApplicationContext(), "Вы выбрали кнопку \"Иду дальше\"!",
                Toast.LENGTH_LONG).show();
    }
    public void onCancelClicked() {
        Toast.makeText(getApplicationContext(), "Вы выбрали кнопку \"Нет\"!",
                Toast.LENGTH_LONG).show();
    }
    public void onNeutralClicked() {
        Toast.makeText(getApplicationContext(), "Вы выбрали кнопку \"На паузе\"!",
                Toast.LENGTH_LONG).show();
    }

    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        String time = hourOfDay + ":" + minute;
        System.out.println("Выбранное время: " + time);
    }

    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "/" + (month + 1) + "/" + year;
        System.out.println("Выбранная дата: " + date);
    }
}
