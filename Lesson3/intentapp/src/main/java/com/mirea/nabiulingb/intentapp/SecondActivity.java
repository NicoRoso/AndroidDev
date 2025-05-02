package com.mirea.nabiulingb.intentapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        TextView tvResult = findViewById(R.id.tvResult);
        Intent intent = getIntent();

        String time = intent.getStringExtra("current_time");
        int squaredNumber = intent.getIntExtra("student_number_squared", 0);

        tvResult.setText(
                "КВАДРАТ ЗНАЧЕНИЯ МОЕГО НОМЕРА ПО СПИСКУ В ГРУППЕ СОСТАВЛЯЕТ " +
                        squaredNumber + ", а текущее время " + time
        );
    }
}