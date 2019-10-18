package com.tebet.mojual.common.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import co.common.util.LanguageUtil
import co.common.view.dialog.SingleChoiceDialog
import com.tebet.mojual.data.models.Language

class LanguageChoiceDialog : SingleChoiceDialog<Language>() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setSelectedIndex(LanguageUtil.instance.getLanguageIndex())
        return super.onCreateDialog(savedInstanceState)
    }

    override fun getListItemAsString(): List<String> {
        return items.map { it.languageName }
    }

    override fun onStart() {
        super.onStart()
        if (dialog is AlertDialog) {
            val nButton = (dialog as AlertDialog).getButton(DialogInterface.BUTTON_NEGATIVE)
            nButton.visibility = View.GONE
        }
    }
}
