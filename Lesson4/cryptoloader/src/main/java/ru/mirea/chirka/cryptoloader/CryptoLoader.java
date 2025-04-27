package ru.mirea.chirka.cryptoloader;

import android.content.Context;
import android.os.Bundle;

import androidx.loader.content.AsyncTaskLoader;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class CryptoLoader extends AsyncTaskLoader<String> {
    private final byte[] encryptedData;
    private final byte[] key;

    public CryptoLoader(Context context, Bundle args) {
        super(context);
        encryptedData = args.getByteArray("ENC_DATA");
        key = args.getByteArray("KEY");
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        SecretKey secretKey = new SecretKeySpec(key, 0, key.length, "AES");

        return CryptoUtils.decryptMsg(encryptedData, secretKey);
    }
}