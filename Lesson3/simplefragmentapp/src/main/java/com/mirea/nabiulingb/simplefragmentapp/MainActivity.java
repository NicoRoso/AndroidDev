package com.mirea.nabiulingb.simplefragmentapp;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private FirstFragment firstFragment;
    private SecondFragment secondFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main);
        } else {
            setContentView(R.layout.activity_main);

            FirstFragment firstFragment = new FirstFragment();
            SecondFragment secondFragment = new SecondFragment();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, firstFragment)
                    .commit();

            findViewById(R.id.btn_first).setOnClickListener(v -> {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, firstFragment)
                        .commit();
            });

            findViewById(R.id.btn_second).setOnClickListener(v -> {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, secondFragment)
                        .commit();
            });
        }
    }
}