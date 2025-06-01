package com.mirea.nabiulingb.mireaproject.ui.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.mirea.nabiulingb.mireaproject.databinding.FragmentNetworkBinding;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkFragment extends Fragment {
    private FragmentNetworkBinding binding;
    private static final String TAG = "NetworkFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNetworkBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnRefresh.setOnClickListener(v -> loadNetworkData());
        loadNetworkData();
    }

    private void loadNetworkData() {
        ConnectivityManager connMgr = (ConnectivityManager) requireContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr != null ? connMgr.getActiveNetworkInfo() : null;

        if (networkInfo != null && networkInfo.isConnected()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.tvData.setText("Загрузка данных...");
            new DownloadIpInfoTask().execute("https://ipinfo.io/json");
        } else {
            Toast.makeText(getContext(), "Нет интернет-соединения", Toast.LENGTH_SHORT).show();
            binding.tvData.setText("Статус: Нет интернета");
        }
    }

    private class DownloadIpInfoTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadContent(urls[0]);
            } catch (IOException e) {
                Log.e(TAG, "Ошибка загрузки: " + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            binding.progressBar.setVisibility(View.GONE);

            if (result != null) {
                try {
                    JSONObject responseJson = new JSONObject(result);
                    String ip = responseJson.optString("ip");
                    String city = responseJson.optString("city");
                    String region = responseJson.optString("region");
                    String country = responseJson.optString("country");
                    String org = responseJson.optString("org");

                    String networkData = "Сетевые данные:\n\n" +
                            "IP: " + ip + "\n" +
                            "Город: " + city + "\n" +
                            "Регион: " + region + "\n" +
                            "Страна: " + country + "\n" +
                            "Организация: " + org;

                    binding.tvData.setText(networkData);
                } catch (JSONException e) {
                    Log.e(TAG, "Ошибка парсинга JSON: " + e.getMessage());
                    binding.tvData.setText("Ошибка обработки данных");
                }
            } else {
                binding.tvData.setText("Ошибка загрузки данных");
            }
        }
    }

    private String downloadContent(String url) throws IOException {
        InputStream inputStream = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                ByteArrayOutputStream result = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    result.write(buffer, 0, length);
                }
                return result.toString("UTF-8");
            } else {
                throw new IOException("HTTP error code: " + responseCode);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}