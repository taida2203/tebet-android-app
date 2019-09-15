package co.common.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.ContextThemeWrapper;

import com.tebet.mojual.common.R;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by heo_local on 1/31/2016.
 */
public abstract class SingleChoiceDialog<T> extends MasbroDialog<T> {
    private List<String> stringName;
    SingleChoiceDialogCallback multipleChoiceDialogCallback;
    private int selectedIndex = 0;
    private int currentItem = 0;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        stringName = new ArrayList<>();
        stringName.addAll(getListItemAsString());

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar));

        // Set the dialog title
        builder.setSingleChoiceItems(stringName.toArray(new CharSequence[stringName.size()]), selectedIndex, (dialog, which) -> {
            currentItem = which;
            selectedIndex = currentItem;
        })
                // Set the action buttons
                .setPositiveButton(R.string.general_button_ok, (dialog, id) -> {
                    /*selectedIndex = currentItem;*/
                    // User clicked OK, so save the mSelectedItems results somewhere
                    // or return them to the component that opened the dialog
                    if (multipleChoiceDialogCallback != null) {
                        multipleChoiceDialogCallback.onOk(getSelectedItem());
                    }
                })
                .setNegativeButton(R.string.general_button_cancel, (dialog, id) -> {
                            if (multipleChoiceDialogCallback != null) {
                                multipleChoiceDialogCallback.onCancel();
                            }
                        }
                );
        return builder.create();
    }

    public abstract List<String> getListItemAsString();

    public SingleChoiceDialog<T> setCallback(SingleChoiceDialogCallback multipleChoiceDialogCallback) {
        this.multipleChoiceDialogCallback = multipleChoiceDialogCallback;
        return this;
    }

    public SingleChoiceDialog setItems(List<T> items) {
        this.items = items;
        return this;
    }

    public SingleChoiceDialog setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
        return this;
    }

    public int getSelectedPosition() {
        return selectedIndex;
    }

    public T getSelectedItem() {
        T selectedItem = null;
        try {
            selectedItem = items.get(selectedIndex);
        } catch (Exception e) {
            Timber.e(e);
        }
        return selectedItem;
    }

    public interface SingleChoiceDialogCallback<T> {
        void onOk(T selectedItem);

        void onCancel();
    }
}
