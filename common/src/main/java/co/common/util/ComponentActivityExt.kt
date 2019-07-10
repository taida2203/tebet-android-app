package co.common.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ComponentActivity

fun ComponentActivity.closeSoftKeyboard(view: View) {
    val imm =
            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}