package com.mirea.nabiulingb.workmanager;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.concurrent.TimeUnit;

public class UploadWorker extends Worker {
    private static final String TAG = "UploadWorker";

    public UploadWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {
        Log.d(TAG, "doWork: начато выполнение фоновой задачи");

        try {
            TimeUnit.SECONDS.sleep(10);

            String inputData = getInputData().getString("KEY_INPUT");
            Log.d(TAG, "Полученные данные: " + inputData);

            Data outputData = new Data.Builder()
                    .putString("KEY_OUTPUT", "Завершено: " + inputData)
                    .build();

            Log.d(TAG, "doWork: задача успешно завершена");
            return Result.success(outputData);

        } catch (InterruptedException e) {
            Log.e(TAG, "Ошибка выполнения задачи", e);
            return Result.failure();
        }
    }
}