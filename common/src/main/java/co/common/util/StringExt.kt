package co.common.util

import android.os.Build
import android.text.Html

fun String.isValidEmail() =
        this.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(this.trim { it <= ' ' }).matches()
fun String.showHtml() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(this)
        }