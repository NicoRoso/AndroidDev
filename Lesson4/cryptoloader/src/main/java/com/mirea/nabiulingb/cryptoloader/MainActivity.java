package com.mirea.nabiulingb.cryptoloader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private final int LoaderID = 1234;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);

        findViewById(R.id.buttonEncrypt).setOnClickListener(v -> {
            String message = editText.getText().toString();
            SecretKey key = CryptoUtils.generateKey();
            byte[] encrypted = CryptoUtils.encryptMsg(message, key);

            Bundle bundle = new Bundle();
            bundle.putByteArray("cipher", encrypted);
            bundle.putByteArray("key", key.getEncoded());

            LoaderManager.getInstance(this).initLoader(LoaderID, bundle, this);
        });
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        return new MyLoader(this, args);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String decrypted) {
        Toast.makeText(this, "Расшифрованная фраза: " + decrypted, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {}
}
