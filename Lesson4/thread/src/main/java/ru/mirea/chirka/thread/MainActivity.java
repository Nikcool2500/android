package ru.mirea.chirka.thread;

import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import ru.mirea.chirka.thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding b;
    private int threadCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        setupMainThreadInfo();
        b.btnCompute.setOnClickListener(v -> launchBackgroundCalculation());
    }

    private void setupMainThreadInfo() {
        Thread main = Thread.currentThread();
        String info = new StringBuilder()
                .append("Поток до: ").append(main.getName()).append("\n")
                .append("Приоритет: ").append(main.getPriority()).append("\n")
                .append("Group: ").append(main.getThreadGroup().getName()).append("\n")
                .toString();

        main.setName("Группа БСБО-04-23, №26, Фильм: Престиж");
        info += "Поток после: " + main.getName() + "\n";

        b.tvLog.setText(info);
        Log.d("MainActivity", "StackTrace: " + Log.getStackTraceString(new Throwable()));
    }

    private void launchBackgroundCalculation() {
        String sPairs = b.etPairs.getText().toString();
        String sDays  = b.etDays .getText().toString();

        if (sPairs.isEmpty() || sDays.isEmpty()) {
            appendLog("Введите оба числа!");
            return;
        }

        int totalPairs, days;
        try {
            totalPairs = Integer.parseInt(sPairs);
            days       = Integer.parseInt(sDays);
            if (days <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            appendLog("Неверный ввод: дни > 0.");
            return;
        }

        int id = threadCount++;
        new Thread(() -> {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

            double avg = (double) totalPairs / days;
            String result = String.format("Поток #%d: среднее = %.2f", id, avg);

            runOnUiThread(() -> appendLog(result));
            Log.d("ThreadCalc", result
                    + ", Group=" + Thread.currentThread().getThreadGroup().getName()
                    + ", Prio=" + Process.getThreadPriority(Process.myTid()));
        }).start();
    }

    private void appendLog(String msg) {
        b.tvLog.append(msg + "\n");
    }
}
