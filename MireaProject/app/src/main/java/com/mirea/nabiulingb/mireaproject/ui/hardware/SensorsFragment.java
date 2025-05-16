package com.mirea.nabiulingb.mireaproject.ui.hardware;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mirea.nabiulingb.mireaproject.R;

public class SensorsFragment extends Fragment implements SensorEventListener {
        private static final String TAG = "SensorsFragment";
        private static final float THRESHOLD_FLAT = 1.5f;
        private static final float GRAVITY_THRESHOLD = 1.0f;

        private SensorManager sensorManager;
    private Sensor accelerometer;
    private TextView textViewStatus;
    private View viewIndicator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sensors, container, false);

        textViewStatus = view.findViewById(R.id.textViewFlatStatus);
        viewIndicator = view.findViewById(R.id.viewFlatIndicator);

        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (accelerometer == null) {
                textViewStatus.setText("Акселерометр не доступен");
            }
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sensorManager != null && accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float norm = (float) Math.sqrt(x*x + y*y + z*z);
            x /= norm;
            y /= norm;
            z /= norm;

            float angle = (float) Math.toDegrees(Math.acos(z));

            boolean isFlat = angle < 15;

            boolean isFlatAlternative = Math.abs(x) < 0.3f &&
                    Math.abs(y) < 0.3f &&
                    Math.abs(z - 1) < 0.2f;

            if (isFlat) {
                textViewStatus.setText("Устройство лежит ровно (" + (int)angle + "°)");
                viewIndicator.setBackgroundColor(Color.GREEN);
            } else {
                textViewStatus.setText("Устройство наклонено (" + (int)angle + "°)");
                viewIndicator.setBackgroundColor(Color.RED);
            }

            Log.d(TAG, String.format("Norm: X=%.2f, Y=%.2f, Z=%.2f, Angle=%.1f°, Flat=%b",
                    x, y, z, angle, isFlat));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Можно добавить обработку изменения точности
    }
}