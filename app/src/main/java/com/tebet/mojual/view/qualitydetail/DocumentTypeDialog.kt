package com.tebet.mojual.view.qualitydetail

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import co.common.util.LanguageUtil
import co.common.view.dialog.SingleChoiceDialog
import com.tebet.mojual.R
import com.tebet.mojual.common.util.toDisplayStatus
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.data.models.enumeration.DocumentType

class DocumentTypeDialog : SingleChoiceDialog<DocumentType>() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setItems(listOf(DocumentType.LOGISTIC_RECEIPT, DocumentType.OTHER))
        return super.onCreateDialog(savedInstanceState)
    }

    override fun getListItemAsString(): List<String> {
        return items!!.map { it.toDisplayStatus() }
    }

    override fun onStart() {
        super.onStart()
        if (dialog is AlertDialog) {
            val nButton = (dialog as AlertDialog).getButton(DialogInterface.BUTTON_NEGATIVE)
            nButton.visibility = View.GONE
        }
    }
}
