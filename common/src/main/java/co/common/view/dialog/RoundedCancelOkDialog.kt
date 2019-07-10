package co.common.view.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tebet.mojual.common.R


@SuppressLint("ValidFragment")
class RoundedCancelOkDialog : RoundedOkDialog, View.OnClickListener {

    override val contentLayout: Int
        get() = R.layout.ok_cancel_default_layout

    constructor(string: String) : super(string) {}

    constructor() : super() {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = super.onCreateView(inflater, container, savedInstanceState)
        addFirstButton(
            RoundedDialogButton(
                resources.getString(R.string.general_button_cancel),
                R.color.dialogColorCancel
            )
        )
        addSecondButton(
            RoundedDialogButton(
                resources.getString(R.string.general_button_ok),
                R.color.dialogColorOK
            )
        )
        return v
    }
}
