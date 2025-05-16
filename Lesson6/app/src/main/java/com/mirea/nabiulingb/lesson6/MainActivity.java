package com.mirea.nabiulingb.lesson6;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText etGroup, etNumber, etMovie;
    private Button btnSave;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etGroup = findViewById(R.id.etGroup);
        etNumber = findViewById(R.id.etNumber);
        etMovie = findViewById(R.id.etMovie);
        btnSave = findViewById(R.id.btnSave);

        sharedPref = getSharedPreferences("mirea_settings", MODE_PRIVATE);

        loadData();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    private void saveData() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("GROUP", etGroup.getText().toString());
        editor.putInt("NUMBER", Integer.parseInt(etNumber.getText().toString()));
        editor.putString("MOVIE", etMovie.getText().toString());
        editor.apply();
        Toast.makeText(this, "Данные сохранены", Toast.LENGTH_SHORT).show();
    }

    private void loadData() {
        etGroup.setText(sharedPref.getString("GROUP", ""));
        etNumber.setText(String.valueOf(sharedPref.getInt("NUMBER", 14)));
        etMovie.setText(sharedPref.getString("MOVIE", ""));
        Toast.makeText(this, "Данные загружены", Toast.LENGTH_SHORT).show();
    }
}