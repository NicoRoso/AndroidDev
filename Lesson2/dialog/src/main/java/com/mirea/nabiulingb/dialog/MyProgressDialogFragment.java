package com.mirea.nabiulingb.dialog;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class MyProgressDialogFragment extends DialogFragment {

    @Override
    public ProgressDialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(true);
        return progressDialog;
    }
}