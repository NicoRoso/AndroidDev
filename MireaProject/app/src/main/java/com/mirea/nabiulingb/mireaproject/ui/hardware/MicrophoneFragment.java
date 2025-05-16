package com.mirea.nabiulingb.mireaproject.ui.hardware;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.mirea.nabiulingb.mireaproject.R;

import java.io.File;
import java.io.IOException;

public class MicrophoneFragment extends Fragment {
    private TextView textViewSoundLevel;
    private ProgressBar progressBarSoundLevel;
    private Button buttonStartMonitoring;

    private MediaRecorder recorder;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private static final int REFRESH_INTERVAL = 100;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    startMonitoring();
                } else {
                    buttonStartMonitoring.setEnabled(false);
                    buttonStartMonitoring.setText("Нет разрешения");
                    Toast.makeText(getContext(), "Доступ к микрофону запрещен", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_microphone, container, false);

        textViewSoundLevel = view.findViewById(R.id.textViewSoundLevel);
        progressBarSoundLevel = view.findViewById(R.id.progressBarSoundLevel);
        buttonStartMonitoring = view.findViewById(R.id.buttonStartMonitoring);

        textViewSoundLevel.setText("Уровень: Н/Д");
        progressBarSoundLevel.setProgress(0);

        buttonStartMonitoring.setOnClickListener(v -> {
            if (recorder == null) {
                checkPermissionAndStartMonitoring();
            } else {
                stopMonitoring();
            }
        });

        return view;
    }

    private void checkPermissionAndStartMonitoring() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED) {
            startMonitoring();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
        }
    }

    private void startMonitoring() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        String tempFilePath = requireContext().getCacheDir().getAbsolutePath() + "/temp_audio.3gp";
        recorder.setOutputFile(tempFilePath);

        try {
            recorder.prepare();
            recorder.start();
            buttonStartMonitoring.setText("Остановить запись");
            handler.post(updateSoundLevel);
            Toast.makeText(getContext(), "Запись начата", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            recorder.release();
            recorder = null;
            buttonStartMonitoring.setText("Начать запись");
            Toast.makeText(getContext(), "Ошибка инициализации", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopMonitoring() {
        handler.removeCallbacks(updateSoundLevel);

        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
        }

        buttonStartMonitoring.setText("Начать запись");
        textViewSoundLevel.setText("Уровень: Остановлен");
        progressBarSoundLevel.setProgress(0);

        File tempFile = new File(requireContext().getCacheDir().getAbsolutePath() + "/temp_audio.3gp");
        if (tempFile.exists()) {
            tempFile.delete();
        }

        Toast.makeText(getContext(), "Запись остановлена", Toast.LENGTH_SHORT).show();
    }

    private final Runnable updateSoundLevel = new Runnable() {
        @Override
        public void run() {
            if (recorder != null) {
                int amplitude = recorder.getMaxAmplitude();
                textViewSoundLevel.setText("Амплитуда: " + amplitude);
                progressBarSoundLevel.setProgress(amplitude);
                handler.postDelayed(this, REFRESH_INTERVAL);
            } else {
                handler.removeCallbacks(this);
                textViewSoundLevel.setText("Уровень: Остановлен");
                progressBarSoundLevel.setProgress(0);
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        stopMonitoring();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateSoundLevel);
    }
}