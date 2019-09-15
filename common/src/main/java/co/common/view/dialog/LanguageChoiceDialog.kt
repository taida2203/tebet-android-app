package co.common.view.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import co.common.util.LanguageUtil
import com.tebet.mojual.common.R

class LanguageChoiceDialog : SingleChoiceDialog<String>() {
    private var listDialog: List<String>? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setSelectedIndex(LanguageUtil.instance.getLanguageIndex())
        listDialog = listOf(
            getString(R.string.support_language_english),
            getString(R.string.support_language_bahasa)
        )
        setItems(listDialog)
        return super.onCreateDialog(savedInstanceState)
    }

    override fun getListItemAsString(): List<String> {
        return listDialog!!
    }

    override fun onStart() {
        super.onStart()
        if (dialog is AlertDialog) {
            val nButton = (dialog as AlertDialog).getButton(DialogInterface.BUTTON_NEGATIVE)
            nButton.visibility = View.GONE
        }
    }
}
