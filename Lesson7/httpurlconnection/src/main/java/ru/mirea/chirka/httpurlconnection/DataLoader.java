package ru.mirea.chirka.httpurlconnection;

import org.json.JSONObject;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DataLoader {
    public interface DataListener {
        void onDataLoaded(JSONObject ipData, JSONObject weatherData);
        void onError(String error);
    }

    private final DataListener listener;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public DataLoader(DataListener listener) {
        this.listener = listener;
    }

    public void loadData() {
        executor.execute(() -> {
            try {
                String ipJson = NetworkUtils.get("https://ipinfo.io/json");
                JSONObject ipData = new JSONObject(ipJson);

                String loc = ipData.getString("loc");
                String[] coords = loc.split(",");

                String weatherUrl = String.format(
                        "https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&current_weather=true",
                        coords[0], coords[1]);

                String weatherJson = NetworkUtils.get(weatherUrl);
                JSONObject weatherData = new JSONObject(weatherJson);

                listener.onDataLoaded(ipData, weatherData);
            } catch (Exception e) {
                listener.onError("Error: " + e.getMessage());
            }
        });
    }
}