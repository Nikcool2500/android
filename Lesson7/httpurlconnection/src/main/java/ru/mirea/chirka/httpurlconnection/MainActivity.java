package ru.mirea.chirka.httpurlconnection;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private TextView tvIp, tvCity, tvRegion, tvCountry, tvLoc, tvWeather;
    private Button btnRefresh;
    private DataLoader dataLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupDataLoader();
        setupButton();
    }

    private void initViews() {
        tvIp = findViewById(R.id.tv_ip);
        tvCity = findViewById(R.id.tv_city);
        tvRegion = findViewById(R.id.tv_region);
        tvCountry = findViewById(R.id.tv_country);
        tvLoc = findViewById(R.id.tv_loc);
        tvWeather = findViewById(R.id.tv_weather);
        btnRefresh = findViewById(R.id.btn_refresh);
    }

    private void setupDataLoader() {
        dataLoader = new DataLoader(new DataLoader.DataListener() {
            @Override
            public void onDataLoaded(JSONObject ipData, JSONObject weatherData) {
                runOnUiThread(() -> updateViews(ipData, weatherData));
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() ->
                        Toast.makeText(MainActivity.this, error, Toast.LENGTH_LONG).show());
            }
        });
    }

    private void setupButton() {
        btnRefresh.setOnClickListener(v -> {
            if(isNetworkAvailable()) {
                dataLoader.loadData();
            } else {
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm != null ? cm.getActiveNetworkInfo() : null;
        return netInfo != null && netInfo.isConnected();
    }

    private void updateViews(JSONObject ipData, JSONObject weatherData) {
        try {
            tvIp.setText("IP: " + ipData.getString("ip"));
            tvCity.setText("City: " + ipData.optString("city"));
            tvRegion.setText("Region: " + ipData.optString("region"));
            tvCountry.setText("Country: " + ipData.optString("country"));
            tvLoc.setText("Location: " + ipData.optString("loc"));

            if(weatherData != null) {
                JSONObject weather = weatherData.getJSONObject("current_weather");
                String temp = weather.getDouble("temperature") + "°C";
                tvWeather.setText("Temperature: " + temp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}