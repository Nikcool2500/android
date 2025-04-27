package ru.mirea.chirka.cryptoloader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<String> {

    private static final int LOADER_ID = 1;
    private EditText etInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etInput = findViewById(R.id.etInput);
    }

    public void onEncryptClick(View view) {
        String input = etInput.getText().toString();
        if (input.isEmpty()) return;

        SecretKey secretKey = CryptoUtils.generateKey();
        byte[] encrypted = CryptoUtils.encryptMsg(input, secretKey);

        Bundle args = new Bundle();
        args.putByteArray("ENC_DATA", encrypted);
        args.putByteArray("KEY", secretKey.getEncoded());

        LoaderManager.getInstance(this).restartLoader(LOADER_ID, args, this);
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new CryptoLoader(this, args);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String decryptedText) {
        Toast.makeText(this, "Decrypted: " + decryptedText, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
    }
}