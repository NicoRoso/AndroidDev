package com.mirea.nabiulingb.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class MyLooper extends Thread {
    private static final String TAG = "MyLooper";
    public Handler mHandler;
    private final Handler mainHandler;

    public MyLooper(Handler mainThreadHandler) {
        this.mainHandler = mainThreadHandler;
    }

    @Override
    public void run() {
        Log.d(TAG, "Запуск фонового потока с Looper");
        Looper.prepare();

        mHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message msg) {
                try {
                    Bundle bundle = msg.getData();
                    int age = bundle.getInt("age");
                    String profession = bundle.getString("profession");

                    Log.d(TAG, String.format("Получено сообщение → Возраст: %d, Профессия: %s",
                            age, profession));

                    TimeUnit.SECONDS.sleep(age);

                    String result = String.format(
                            "Готово! Возраст: %d, Профессия: %s, Обработано за %d сек.",
                            age, profession, age
                    );

                    Log.d(TAG, "Результат обработки: " + result);

                    Message resultMsg = Message.obtain();
                    Bundle resultBundle = new Bundle();
                    resultBundle.putString("result", result);
                    resultMsg.setData(resultBundle);
                    mainHandler.sendMessage(resultMsg);

                } catch (InterruptedException e) {
                    Log.e(TAG, "Поток был прерван", e);
                    Thread.currentThread().interrupt();
                }
            }
        };

        Log.d(TAG, "Looper готов к работе");
        Looper.loop();

        Log.d(TAG, "Looper завершил работу");
    }
}