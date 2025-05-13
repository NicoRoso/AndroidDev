package com.mirea.nabiulingb.cryptoloader;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MyLoader extends AsyncTaskLoader<String> {
    private byte[] cipherText;
    private byte[] key;

    public MyLoader(@NonNull Context context, Bundle args) {
        super(context);
        if (args != null) {
            cipherText = args.getByteArray("cipher");
            key = args.getByteArray("key");
        }
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public String loadInBackground() {
        SecretKey originalKey = new SecretKeySpec(key, 0, key.length, "AES");
        return CryptoUtils.decryptMsg(cipherText, originalKey);
    }
}
