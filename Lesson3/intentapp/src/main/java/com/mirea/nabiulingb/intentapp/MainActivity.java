package com.mirea.nabiulingb.intentapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSendData = findViewById(R.id.btnSendData);
        btnSendData.setOnClickListener(v -> {
            // Получаем текущее время
            long dateInMillis = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = sdf.format(new Date(dateInMillis));

            // Передаем данные в SecondActivity
            Intent intent = new Intent(this, SecondActivity.class);
            intent.putExtra("current_time", dateString);
            intent.putExtra("student_number_squared", 14 * 14);
            startActivity(intent);
        });
    }
}