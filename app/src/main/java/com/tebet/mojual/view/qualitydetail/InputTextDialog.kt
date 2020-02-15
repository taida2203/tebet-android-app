package com.tebet.mojual.view.qualitydetail

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment

/**
 * Created by User on 5/12/2017.
 */
class InputTextDialog : DialogFragment() {
    private var editText: EditText? = null
    private var dialogCallback: DialogCallback? =
        null

    fun setCallback(callback: DialogCallback?): InputTextDialog {
        dialogCallback = callback
        return this
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(
            ContextThemeWrapper(
                activity,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar
            )
        )
        val view = activity!!.layoutInflater
            .inflate(com.tebet.mojual.R.layout.custom_dialog_input_text, null)
        editText = view.findViewById(com.tebet.mojual.R.id.edit_invitation_code)
        builder.setView(view)
        //        builder.setTitle(getString(R.string.invitation));
        builder.setPositiveButton(
            com.tebet.mojual.R.string.general_button_ok
        ) { dialog: DialogInterface?, id: Int ->
            if (dialogCallback != null) {
                dialogCallback!!.onOk(editText!!.text.toString().trim { it <= ' ' })
            }
        }
        builder.setNegativeButton(
            com.tebet.mojual.R.string.general_button_cancel
        ) { dialog: DialogInterface?, id: Int ->
            if (dialogCallback != null) {
                dialogCallback!!.onCancel()
            }
        }
        return builder.create()
    }

    override fun onStart() {
        super.onStart()
        var pButton: Button? = null
        var nButton: Button? = null
        if (dialog is AlertDialog) {
            pButton =
                (dialog as AlertDialog?)!!.getButton(DialogInterface.BUTTON_POSITIVE)
            nButton =
                (dialog as AlertDialog?)!!.getButton(DialogInterface.BUTTON_NEGATIVE)
        }
        if (pButton != null) {
            pButton.setBackgroundColor(
                ContextCompat.getColor(
                    activity!!,
                    com.tebet.mojual.R.color.green_dark
                )
            )
            pButton.setTextColor(
                ContextCompat.getColor(
                    activity!!,
                    com.tebet.mojual.R.color.white
                )
            )
        }
        if (nButton != null) {
            nButton.setBackgroundColor(
                ContextCompat.getColor(
                    activity!!,
                    com.tebet.mojual.R.color.colorPrimary
                )
            )
            nButton.setTextColor(
                ContextCompat.getColor(
                    activity!!,
                    com.tebet.mojual.R.color.white
                )
            )
        }
    }

    interface DialogCallback {
        fun onOk(text: String?)
        fun onCancel()
    }

    override fun onDismiss(dialog: DialogInterface) {
        val imm =
            editText!!.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive) imm.toggleSoftInput(
            0,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
        super.onDismiss(dialog)
    }
}