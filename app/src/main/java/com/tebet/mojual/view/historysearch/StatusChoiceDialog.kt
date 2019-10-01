package com.tebet.mojual.view.historysearch

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
import com.tebet.mojual.data.models.enumeration.OrderStatus

class StatusChoiceDialog : SingleChoiceDialog<OrderStatus>() {
    private var listDialog: List<OrderStatus>? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        listDialog = listOf(OrderStatus.ALL, OrderStatus.OPEN, OrderStatus.CLOSED, OrderStatus.REJECTED)
        setItems(listDialog)
        return super.onCreateDialog(savedInstanceState)
    }

    override fun getListItemAsString(): List<String> {
        return listDialog!!.map { it.toDisplayStatus() }
    }

    override fun onStart() {
        super.onStart()
        if (dialog is AlertDialog) {
            val nButton = (dialog as AlertDialog).getButton(DialogInterface.BUTTON_NEGATIVE)
            nButton.visibility = View.GONE
        }
    }
}
