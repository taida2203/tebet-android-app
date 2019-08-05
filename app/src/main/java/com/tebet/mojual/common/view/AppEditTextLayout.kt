package com.tebet.mojual.common.view

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout
import com.tebet.mojual.R

open class AppEditTextLayout : TextInputLayout {
    constructor(context: Context) : super(context) {
        setFont(null)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        setFont(attributeSet)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int)
            : super(context, attributeSet, R.style.TextInputLayoutStyle) {
        setFont(attributeSet)
    }

    private fun setFont(attributeSet: AttributeSet?) {
        this.background = ContextCompat.getDrawable(context, R.drawable.border_edit_text)
        isHintEnabled = false
    }
}