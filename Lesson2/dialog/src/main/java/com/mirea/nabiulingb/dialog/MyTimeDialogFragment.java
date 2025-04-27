package com.mirea.nabiulingb.dialog;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

public class MyTimeDialogFragment extends DialogFragment {

    @Override
    public TimePickerDialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(
                requireActivity(),
                (view, hourOfDay, minuteOfHour) -> {
                    String time = String.format("%02d:%02d", hourOfDay, minuteOfHour);
                    showSnackbar("Выбрано время: " + time);
                },
                hour,
                minute,
                DateFormat.is24HourFormat(requireContext())
        );
    }

    private void showSnackbar(String message) {
        if (getActivity() != null) {
            Snackbar.make(
                    getActivity().findViewById(android.R.id.content),
                    message,
                    Snackbar.LENGTH_SHORT
            ).show();
        }
    }
}