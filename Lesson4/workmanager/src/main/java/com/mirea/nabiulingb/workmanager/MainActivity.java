package com.mirea.nabiulingb.workmanager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startButton = findViewById(R.id.start_button);
        TextView statusText = findViewById(R.id.status_text);

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .build();

        Data inputData = new Data.Builder()
                .putString("KEY_INPUT", "Фоновая задача от студента Глеба")
                .build();

        WorkRequest uploadWorkRequest = new OneTimeWorkRequest.Builder(UploadWorker.class)
                .setInputData(inputData)
                .setConstraints(constraints)
                .build();

        startButton.setOnClickListener(v -> {
            statusText.setText("Задача запущена...");
            Log.d(TAG, "Запуск фоновой задачи");

            WorkManager.getInstance(this)
                    .enqueue(uploadWorkRequest);

            WorkManager.getInstance(this)
                    .getWorkInfoByIdLiveData(uploadWorkRequest.getId())
                    .observe(this, workInfo -> {
                        if (workInfo != null) {
                            String status = "Статус: " + workInfo.getState().name();
                            statusText.setText(status);
                            Log.d(TAG, status);

                            if (workInfo.getState().isFinished()) {
                                String output = workInfo.getOutputData().getString("KEY_OUTPUT");
                                statusText.append("\n" + output);
                                Log.d(TAG, "Результат: " + output);
                            }
                        }
                    });
        });
    }
}