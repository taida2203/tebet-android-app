package com.tebet.mojual.view.qualitydetail

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.tebet.mojual.common.util.GlideApp
import com.tebet.mojual.data.models.OrderDocument

class DocumentDialog(private val document: OrderDocument) : DialogFragment() {
    private var dialogCallback: DialogCallback? =
        null

    fun setCallback(callback: DialogCallback?): DocumentDialog {
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
            .inflate(com.tebet.mojual.R.layout.custom_dialog_view_document, null)
        val imageView:ImageView = view.findViewById(com.tebet.mojual.R.id.img_document)
        builder.setView(view)
        //        builder.setTitle(getString(R.string.invitation));
        builder.setPositiveButton(
            com.tebet.mojual.R.string.general_button_ok
        ) { dialog: DialogInterface?, id: Int ->
            if (dialogCallback != null) {
                dialogCallback!!.onOk()
            }
        }
        GlideApp.with(context!!).load(document.filePath)
            .error(
                ContextCompat.getDrawable(
                    context!!,
                    com.tebet.mojual.R.drawable.signup_avatar_blank
                )
            )
            .placeholder(
                ContextCompat.getDrawable(
                    context!!,
                    com.tebet.mojual.R.drawable.signup_avatar_blank
                )
            )
            .centerCrop()
            .into(imageView)
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
        fun onOk()
    }
}