package com.mirea.nabiulingb.notebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSION = 100;
    private EditText etFileName, etQuote;
    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etFileName = findViewById(R.id.etFileName);
        etQuote = findViewById(R.id.etQuote);
        tvStatus = findViewById(R.id.tvStatus);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnLoad = findViewById(R.id.btnLoad);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION);
        }

        btnSave.setOnClickListener(v -> saveToFile());
        btnLoad.setOnClickListener(v -> loadFromFile());
    }

    private void saveToFile() {
        String fileName = etFileName.getText().toString() + ".txt";
        String quote = etQuote.getText().toString();

        if (fileName.isEmpty() || quote.isEmpty()) {
            tvStatus.setText("Ошибка: заполните все поля");
            return;
        }

        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, fileName);
        try (FileOutputStream fos = new FileOutputStream(file);
             FileOutputStream writer = new FileOutputStream(file)) {
            writer.write(quote.getBytes(StandardCharsets.UTF_8));
            tvStatus.setText("Сохранено в: " + file.getAbsolutePath());
            Toast.makeText(this, "Файл сохранён!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            tvStatus.setText("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadFromFile() {
        String fileName = etFileName.getText().toString() + ".txt";

        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(dir, fileName);

        if (!file.exists()) {
            tvStatus.setText("Ошибка: файл не найден");
            return;
        }

        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            etQuote.setText(content.toString());
            tvStatus.setText("Загружено из: " + file.getName());
        } catch (IOException e) {
            tvStatus.setText("Ошибка чтения: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Разрешения получены", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Разрешения отклонены", Toast.LENGTH_SHORT).show();
            }
        }
    }
}