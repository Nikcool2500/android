package ru.mirea.chirkans.mireaproject.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkInfo;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import ru.mirea.chirkans.mireaproject.R;

public class BackgroundTaskFragment extends Fragment {

    private ProgressBar progressBar;
    private TextView tvStatus;
    private WorkManager workManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_background_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Инициализация элементов
        progressBar = view.findViewById(R.id.progress_bar);
        tvStatus = view.findViewById(R.id.tv_status);
        Button btnStart = view.findViewById(R.id.btn_start);
        workManager = WorkManager.getInstance(requireContext());

        // Обработчик кнопки
        btnStart.setOnClickListener(v -> startDataProcessing());
    }

    private void startDataProcessing() {
        // Настройка ограничений
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build();

        // Создание запроса
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(DataProcessorWorker.class)
                .setConstraints(constraints)
                .build();

        // Визуальные изменения
        progressBar.setVisibility(View.VISIBLE);
        tvStatus.setText("Status: Initializing...");

        // Запуск задачи
        workManager.enqueue(workRequest);

        // Отслеживание статуса
        workManager.getWorkInfoByIdLiveData(workRequest.getId())
                .observe(getViewLifecycleOwner(), workInfo -> {
                    if (workInfo != null) {
                        handleWorkStatus(workInfo.getState());
                    }
                });
    }

    private void handleWorkStatus(WorkInfo.State state) {
        switch (state) {
            case ENQUEUED:
                tvStatus.setText("Status: Waiting in queue");
                break;
            case RUNNING:
                tvStatus.setText("Status: Processing data...");
                progressBar.setIndeterminate(true);
                break;
            case SUCCEEDED:
                progressBar.setVisibility(View.INVISIBLE);
                tvStatus.setText("Status: Complete!");
                break;
            case FAILED:
                progressBar.setVisibility(View.INVISIBLE);
                tvStatus.setText("Status: Error occurred");
                break;
        }
    }
}
