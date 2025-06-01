package com.mirea.nabiulingb.httpurlconnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "HttpURLConnectionApp";

    private TextView tvIp, tvCity, tvRegion, tvCountry, tvCoordinates, tvWeather, tvStatus;
    private Button btnGetData;

    private String ip, city, region, country, latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvIp = findViewById(R.id.tvIp);
        tvCity = findViewById(R.id.tvCity);
        tvRegion = findViewById(R.id.tvRegion);
        tvCountry = findViewById(R.id.tvCountry);
        tvCoordinates = findViewById(R.id.tvCoordinates);
        tvWeather = findViewById(R.id.tvWeather);
        tvStatus = findViewById(R.id.tvStatus);
        btnGetData = findViewById(R.id.btnGetData);

        btnGetData.setOnClickListener(v -> {
            // Проверка подключения к интернету
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                tvStatus.setText("Загрузка данных IP...");
                new DownloadIpInfoTask().execute("https://ipinfo.io/json");
            } else {
                Toast.makeText(this, "Нет подключения к интернету", Toast.LENGTH_SHORT).show();
                tvStatus.setText("Статус: Нет интернета");
            }
        });

        tvStatus.setText("Статус: Ожидание действий");
    }

    private class DownloadIpInfoTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadDataFromUrl(urls[0]);
            } catch (IOException e) {
                Log.e(TAG, "Ошибка загрузки данных: " + e.getMessage());
                return "Ошибка: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject responseJson = new JSONObject(result);

                // Извлекаем данные IP
                ip = responseJson.optString("ip");
                city = responseJson.optString("city");
                region = responseJson.optString("region");
                country = responseJson.optString("country");

                // Обрабатываем координаты
                String loc = responseJson.optString("loc");
                if (loc != null && loc.contains(",")) {
                    String[] coords = loc.split(",");
                    latitude = coords[0];
                    longitude = coords[1];
                }

                // Обновляем UI
                tvIp.setText("IP: " + ip);
                tvCity.setText("Город: " + city);
                tvRegion.setText("Регион: " + region);
                tvCountry.setText("Страна: " + country);

                if (latitude != null && longitude != null) {
                    tvCoordinates.setText("Координаты: " + latitude + ", " + longitude);
                    tvStatus.setText("Загрузка данных погоды...");

                    // Запрашиваем погоду по координатам
                    String weatherUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude +
                            "&longitude=" + longitude + "&current_weather=true";
                    new DownloadWeatherTask().execute(weatherUrl);
                } else {
                    tvCoordinates.setText("Координаты: недоступны");
                    tvWeather.setText("Погода: данные недоступны (нет координат)");
                    tvStatus.setText("Статус: Данные IP загружены, но координаты отсутствуют");
                }

            } catch (JSONException e) {
                Log.e(TAG, "Ошибка парсинга JSON: " + e.getMessage());
                tvStatus.setText("Ошибка обработки данных IP");
            }
        }
    }

    private class DownloadWeatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadDataFromUrl(urls[0]);
            } catch (IOException e) {
                Log.e(TAG, "Ошибка загрузки погоды: " + e.getMessage());
                return "Ошибка: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject weatherJson = new JSONObject(result);

                if (weatherJson.has("current_weather")) {
                    JSONObject currentWeather = weatherJson.getJSONObject("current_weather");
                    double temperature = currentWeather.getDouble("temperature");
                    double windspeed = currentWeather.getDouble("windspeed");
                    int weathercode = currentWeather.getInt("weathercode");

                    String weatherInfo = String.format(
                            "Температура: %.1f°C\nСкорость ветра: %.1f км/ч\nКод погоды: %d",
                            temperature, windspeed, weathercode
                    );
                    tvWeather.setText(weatherInfo);
                    tvStatus.setText("Статус: Все данные успешно загружены");
                } else {
                    tvWeather.setText("Погода: данные недоступны");
                    tvStatus.setText("Статус: Данные погоды не найдены в ответе");
                }

            } catch (JSONException e) {
                Log.e(TAG, "Ошибка парсинга погоды: " + e.getMessage());
                tvWeather.setText("Погода: ошибка обработки данных");
                tvStatus.setText("Статус: Ошибка обработки данных погоды");
            }
        }
    }

    private String downloadDataFromUrl(String url) throws IOException {
        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                return response.toString();
            } else {
                throw new IOException("HTTP error code: " + responseCode);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}