package com.mirea.nabiulingb.mireaproject.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.mirea.nabiulingb.mireaproject.R;

public class ProfileFragment extends Fragment {

    private EditText etName, etAge, etEmail, etPhone;
    private Button btnSave;
    private SharedPreferences sharedPreferences;

    private static final String PROFILE_PREFS = "profile_prefs";
    private static final String KEY_NAME = "name";
    private static final String KEY_AGE = "age";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        etName = view.findViewById(com.mirea.nabiulingb.mireaproject.R.id.et_name);
        etAge = view.findViewById(R.id.et_age);
        etEmail = view.findViewById(R.id.et_email);
        etPhone = view.findViewById(R.id.et_phone);
        btnSave = view.findViewById(R.id.btn_save);

        sharedPreferences = requireActivity().getSharedPreferences(PROFILE_PREFS, Context.MODE_PRIVATE);

        loadProfileData();

        btnSave.setOnClickListener(v -> saveProfileData());

        return view;
    }

    private void loadProfileData() {
        etName.setText(sharedPreferences.getString(KEY_NAME, ""));
        etAge.setText(sharedPreferences.getString(KEY_AGE, ""));
        etEmail.setText(sharedPreferences.getString(KEY_EMAIL, ""));
        etPhone.setText(sharedPreferences.getString(KEY_PHONE, ""));
    }

    private void saveProfileData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_NAME, etName.getText().toString());
        editor.putString(KEY_AGE, etAge.getText().toString());
        editor.putString(KEY_EMAIL, etEmail.getText().toString());
        editor.putString(KEY_PHONE, etPhone.getText().toString());
        editor.apply();

        Toast.makeText(getContext(), "Профиль сохранен", Toast.LENGTH_SHORT).show();
    }
}