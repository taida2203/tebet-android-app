package co.common.view.dialog;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import com.tebet.mojual.common.R;

import java.util.List;

/**
 * Created by heo on 4/8/17.
 */

public class MasbroDialog<T> extends DialogFragment {
    protected List<T> items;

    @Override
    public void onStart() {
        super.onStart();

        Button pButton = null;
        Button nButton = null;
        if (getDialog() instanceof AlertDialog) {
            pButton = ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_POSITIVE);
            nButton = ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_NEGATIVE);
        } else if (getDialog() instanceof DatePickerDialog) {
            pButton = ((DatePickerDialog) getDialog()).getButton(DialogInterface.BUTTON_POSITIVE);
            pButton.setText(R.string.general_button_ok);
            nButton = ((DatePickerDialog) getDialog()).getButton(DialogInterface.BUTTON_NEGATIVE);
            nButton.setText(R.string.general_button_cancel);
        }

        if (pButton != null) {
            pButton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.dialogColorOK));
            pButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.dialogColorBaseWhite));
        }

        if (nButton != null) {
            nButton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.dialogColorOK));
            nButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.dialogColorBaseWhite));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
