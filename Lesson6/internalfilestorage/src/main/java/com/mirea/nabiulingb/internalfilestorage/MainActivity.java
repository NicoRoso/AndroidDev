package com.mirea.nabiulingb.internalfilestorage;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String FILE_NAME = "historical_date.txt";
    private EditText etDate, etDescription;
    private TextView tvFileContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etDate = findViewById(R.id.etDate);
        etDescription = findViewById(R.id.etDescription);
        tvFileContent = findViewById(R.id.tvFileContent);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnLoad = findViewById(R.id.btnLoad);

        btnSave.setOnClickListener(v -> saveToFile());

        btnLoad.setOnClickListener(v -> loadFromFile());
    }

    private void saveToFile() {
        String date = etDate.getText().toString();
        String description = etDescription.getText().toString();
        String data = date + "\n\n" + description;

        try (FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE)) {
            fos.write(data.getBytes());
            Toast.makeText(this, "Файл сохранён", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Ошибка сохранения: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void loadFromFile() {
        try (FileInputStream fis = openFileInput(FILE_NAME)) {
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            String content = new String(buffer);
            tvFileContent.setText(content);
            Toast.makeText(this, "Файл загружен", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Файл не найден или ошибка чтения", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}