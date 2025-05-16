package com.mirea.nabiulingb.mireaproject.ui.hardware;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.mirea.nabiulingb.mireaproject.R;

public class HardwareFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hardware, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btn_compass).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_hardwareFragment_to_compassFragment));

        view.findViewById(R.id.btn_camera).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_hardwareFragment_to_cameraFragment));

        view.findViewById(R.id.btn_mic).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_hardwareFragment_to_microphoneFragment));

        view.findViewById(R.id.btn_sensors).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_hardwareFragment_to_sensorsFragment));
    }
}