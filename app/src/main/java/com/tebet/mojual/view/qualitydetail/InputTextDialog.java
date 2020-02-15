package com.tebet.mojual.view.qualitydetail;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.tebet.mojual.R;

/**
 * Created by User on 5/12/2017.
 */

public class InputTextDialog extends DialogFragment {
    private EditText editText;

    private DialogCallback dialogCallback;

    public InputTextDialog setCallback(DialogCallback callback) {
        this.dialogCallback = callback;
        return this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar));
        View view = getActivity().getLayoutInflater().inflate(R.layout.custom_dialog_input_text, null);
        editText = view.findViewById(R.id.edit_invitation_code);
        builder.setView(view);

//        builder.setTitle(getString(R.string.invitation));
        builder.setPositiveButton(R.string.general_button_ok, (dialog, id) -> {
            if (dialogCallback != null) {
                dialogCallback.onOk(editText.getText().toString().trim());
            }
        });
        builder.setNegativeButton(R.string.general_button_cancel, (dialog, id) -> {
            if (dialogCallback != null) {
                dialogCallback.onCancel();
            }
        });

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        Button pButton = null, nButton = null;
        if (getDialog() instanceof AlertDialog) {
            pButton = ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_POSITIVE);
            nButton = ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_NEGATIVE);
        }

        if (pButton != null) {
            pButton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.green_dark));
            pButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        }

        if (nButton != null) {
            nButton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            nButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        }
    }

    public interface DialogCallback {
        void onOk(String text);

        void onCancel();
    }
}
