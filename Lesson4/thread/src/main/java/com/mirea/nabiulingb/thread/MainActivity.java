package com.mirea.nabiulingb.thread;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;

import com.mirea.nabiulingb.thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View rootView = binding.getRoot();
        setContentView(rootView);

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView mainThreadInfoTextView = binding.textViewMainThreadInfo;
        Thread mainThread = Thread.currentThread();
        mainThreadInfoTextView.setText("Имя текущего потока: " + mainThread.getName());
        mainThread.setName("МОЙ НОМЕР ГРУППЫ: БСБО-07-22, НОМЕР ПО СПИСКУ: 14, МОЙ ЛЮБИМЫЙ ФИЛЬМ: Терминатор 2: Судный день");
        mainThreadInfoTextView.append("\nНовое имя потока: " + mainThread.getName());

        Log.d(MainActivity.class.getSimpleName(), "Stack: " + Arrays.toString(mainThread.getStackTrace()));
        Log.d(MainActivity.class.getSimpleName(), "Group: " + mainThread.getThreadGroup());

        binding.buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int numberThread = counter++;
                        Log.d("ThreadProject", String.format("Запущен поток № %d студентом группы № %s номер по списку № %d ",
                                numberThread, "БСБО-07-22", 14));

                        String totalPairsStr = binding.editTextTotalPairs.getText().toString();
                        String studyDaysStr = binding.editTextStudyDays.getText().toString();

                        int totalPairs = Integer.parseInt(totalPairsStr);
                        int studyDays = Integer.parseInt(studyDaysStr);
                        double averagePairsPerDay = (double) totalPairs / studyDays;
                        String resultMessage = String.format("Среднее количество пар в день: %.2f", averagePairsPerDay);

                        long endTime = System.currentTimeMillis() + 20 * 1000;

                        while (System.currentTimeMillis() < endTime) {
                            synchronized (this) {
                                try {
                                    wait(endTime - System.currentTimeMillis());
                                    Log.d(MainActivity.class.getSimpleName(), "Endtime: " + endTime);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            Log.d("ThreadProject", "Выполнен поток № " + numberThread);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                binding.textViewResult.setText(resultMessage);
                            }
                        });
                    }
                }).start();
            }
        });
    }
}