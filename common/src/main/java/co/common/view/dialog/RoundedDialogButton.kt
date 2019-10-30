package co.common.view.dialog

import com.tebet.mojual.common.R


/**
 * Created by caheo on 11/9/16.
 */

class RoundedDialogButton {
    var buttonText: String
    var color = R.drawable.rounded_bg_button

    constructor(buttonText: String) {
        this.buttonText = buttonText
    }

    constructor(buttonText: String, colorId: Int) {
        this.buttonText = buttonText
        this.color = colorId
    }
}
