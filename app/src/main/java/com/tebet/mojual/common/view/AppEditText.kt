package com.tebet.mojual.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.tebet.mojual.R
import com.tebet.mojual.common.util.Utility

open class AppEditText : TextInputEditText {
    constructor(context: Context) : super(context) {
        setFont(null)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        setFont(attributeSet)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int)
            : super(context, attributeSet, defStyle) {
        setFont(attributeSet)
    }

    open fun setFont(attributeSet: AttributeSet?) {
        this.background = ContextCompat.getDrawable(context, R.drawable.border_edit_text)
        this.setPadding(Utility.dpToPx(20), Utility.dpToPx(8), Utility.dpToPx(20), Utility.dpToPx(8))
        this.setHintTextColor(ContextCompat.getColor(context, R.color.grey_hint))
        setLines(1)
        if (inputType != 129) {
            setSingleLine(true)
        }
    }
}