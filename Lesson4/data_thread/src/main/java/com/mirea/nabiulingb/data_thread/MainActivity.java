package com.mirea.nabiulingb.data_thread;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mirea.nabiulingb.data_thread.databinding.ActivityMainBinding;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvInfo.setText("Последовательность выполнения:\n\n");

        binding.btnStart.setOnClickListener(v -> {
            Runnable runn1 = () -> {
                Log.d("ThreadDemo", "Runnable 1: runOnUiThread (выполнен сразу)");
                binding.tvInfo.append("1. runOnUiThread() - выполнен сразу в UI-потоке\n");
            };

            Runnable runn2 = () -> {
                Log.d("ThreadDemo", "Runnable 2: post (добавлен в очередь UI)");
                binding.tvInfo.append("2. post() - добавлен в очередь UI и выполнен после runOnUiThread\n");
            };

            Runnable runn3 = () -> {
                Log.d("ThreadDemo", "Runnable 3: postDelayed (выполнен с задержкой 2 сек)");
                binding.tvInfo.append("3. postDelayed() - выполнен через 2000 мс после post()\n");
            };

            new Thread(() -> {
                try {
                    Log.d("ThreadDemo", "Фоновый поток начал работу");

                    Thread.sleep(2000);
                    runOnUiThread(runn1);

                    Thread.sleep(1000);
                    binding.tvInfo.post(runn2);

                    binding.tvInfo.postDelayed(runn3, 2000);

                } catch (InterruptedException e) {
                    Log.e("ThreadDemo", "Ошибка потока", e);
                }
            }).start();
        });
    }
}