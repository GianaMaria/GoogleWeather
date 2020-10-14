package com.example.googleauthfirebase.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.googleauthfirebase.R;
import com.example.googleauthfirebase.MainActivity;

public class DialogBuilderFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.title_dialog)
                .setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                        ((MainActivity) getActivity()).onDialogResult(getString(R.string.positive_button));
                    }
                })
                .setMessage(R.string.message_dialog);
        return builder.create();
    }
}
