package com.mirea.nabiulingb.mireaproject.ui.work;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends Worker {

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("MyWorker", "doWork: Задача запущена...");

        try {
            // Эмуляция загрузки данных
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return Result.failure();
        }

        Log.d("MyWorker", "doWork: Задача завершена успешно.");
        return Result.success();
    }
}
