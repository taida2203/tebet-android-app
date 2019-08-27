package co.common.view.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tebet.mojual.common.R


/**
 * Created by caheo on 8/10/16.
 */
@SuppressLint("ValidFragment")
open class RoundedOkDialog : RoundedDialog, View.OnClickListener {

    constructor(messageContent: String) : super(messageContent) {}

    constructor() : super() {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = super.onCreateView(inflater, container, savedInstanceState)
        addFirstButton(
            RoundedDialogButton(
                resources.getString(R.string.general_button_ok),
                R.drawable.rounded_bg_button
            )
        )
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateDialogMode()
    }
}
