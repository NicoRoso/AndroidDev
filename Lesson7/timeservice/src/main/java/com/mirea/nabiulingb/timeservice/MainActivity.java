package com.mirea.nabiulingb.timeservice;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TimeService";
    private TextView textView;
    private final String host = "time.nist.gov";
    private final int port = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.Text);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetTimeTask().execute();
            }
        });
    }

    private class GetTimeTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String timeResult = "";
            try {
                Socket socket = new Socket(host, port);
                BufferedReader reader = SocketUtils.getReader(socket);
                reader.readLine();
                timeResult = reader.readLine();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
                return "Ошибка подключения: " + e.getMessage();
            }
            return timeResult;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null && !result.isEmpty()) {
                textView.setText(parseTime(result));
            } else {
                Toast.makeText(MainActivity.this, "Не удалось получить время", Toast.LENGTH_SHORT).show();
            }
        }

        private String parseTime(String timeString) {
            String[] parts = timeString.split(" ");
            if (parts.length >= 3) {
                return "Дата: " + parts[1] + "\nВремя: " + parts[2];
            }
            return timeString;
        }
    }
}