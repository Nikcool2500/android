package ru.mirea.chirkans.mireaproject.ui;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.util.concurrent.TimeUnit;

public class DataProcessorWorker extends Worker {
    private static final String TAG = "DataProcessor";

    public DataProcessorWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            for (int i = 1; i <= 5; i++) {
                TimeUnit.SECONDS.sleep(2);
                android.util.Log.d(TAG, "Processing stage: " + i + "/5");
            }
            return Result.success();
        } catch (InterruptedException e) {
            return Result.failure();
        }
    }
}
