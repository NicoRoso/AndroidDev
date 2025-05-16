package com.mirea.nabiulingb.mireaproject.ui.hardware;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.mirea.nabiulingb.mireaproject.R;

public class CompassFragment extends Fragment implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private ImageView compassImage;
    private TextView degreeText;
    private TextView directionText;

    private float[] gravity = new float[3];
    private float[] geomagnetic = new float[3];
    private float currentDegree = 0f;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compass, container, false);

        compassImage = view.findViewById(R.id.compass_image);
        degreeText = view.findViewById(R.id.degree_text);
        directionText = view.findViewById(R.id.direction_text);

        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            gravity = event.values.clone();
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            geomagnetic = event.values.clone();
        }

        if (gravity != null && geomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];

            if (SensorManager.getRotationMatrix(R, I, gravity, geomagnetic)) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);

                float azimuthInRadians = orientation[0];
                float azimuthInDegrees = (float) Math.toDegrees(azimuthInRadians);

                if (azimuthInDegrees < 0) {
                    azimuthInDegrees += 360;
                }

                updateCompass(azimuthInDegrees);
            }
        }
    }

    private void updateCompass(float degree) {
        degreeText.setText("Угол: " + Math.round(degree) + "°");

        RotateAnimation rotateAnim = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
        );

        rotateAnim.setDuration(210);
        rotateAnim.setFillAfter(true);
        compassImage.startAnimation(rotateAnim);
        currentDegree = -degree;

        String direction;
        if (degree < 45 || degree > 315) {
            direction = "Север";
        } else if (degree >= 45 && degree <= 135) {
            direction = "Восток";
        } else if (degree > 135 && degree <= 225) {
            direction = "Юг";
        } else {
            direction = "Запад";
        }

        directionText.setText("Направление: " + direction);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}