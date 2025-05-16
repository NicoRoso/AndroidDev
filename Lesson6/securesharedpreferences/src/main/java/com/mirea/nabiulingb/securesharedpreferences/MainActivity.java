package com.mirea.nabiulingb.securesharedpreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText etPoetName = findViewById(R.id.etPoetName);
        ImageView ivPoetImage = findViewById(R.id.ivPoetImage);
        Button btnSave = findViewById(R.id.btnSave);

        ivPoetImage.setImageResource(R.drawable.pushkin);

        try {
            String mainKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

            SharedPreferences secureSharedPreferences = EncryptedSharedPreferences.create(
                    "secret_shared_prefs",
                    mainKeyAlias,
                    getApplicationContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            String savedName = secureSharedPreferences.getString("poet_name", "");
            etPoetName.setText(savedName);

            btnSave.setOnClickListener(v -> {
                String poetName = etPoetName.getText().toString();
                secureSharedPreferences.edit()
                        .putString("poet_name", poetName)
                        .apply();
                Toast.makeText(this, "Данные сохранены безопасно!", Toast.LENGTH_SHORT).show();
            });

        } catch (GeneralSecurityException | IOException e) {
            Toast.makeText(this, "Ошибка шифрования: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}