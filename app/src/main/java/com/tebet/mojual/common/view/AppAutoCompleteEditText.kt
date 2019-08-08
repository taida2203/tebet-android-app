package com.tebet.mojual.common.view

import android.content.Context
import android.util.AttributeSet
import android.widget.AutoCompleteTextView
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.tebet.mojual.R
import android.graphics.drawable.Drawable
import com.tebet.mojual.common.util.Utility


open class AppAutoCompleteEditText : AutoCompleteTextView {
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

    private fun setFont(attributeSet: AttributeSet?) {
        this.background = ContextCompat.getDrawable(context, R.drawable.border_edit_text)
        this.setPadding(Utility.dpToPx(20), Utility.dpToPx(8), Utility.dpToPx(20), Utility.dpToPx(8))
        this.setHintTextColor(ContextCompat.getColor(context, R.color.grey))
        setLines(1)
        setSingleLine(true)
        val img = ContextCompat.getDrawable(context, R.drawable.signup_dropdown_btn)
        img?.setBounds( 60, 60, 60, 60 )
        setCompoundDrawablesWithIntrinsicBounds(null, null, img, null)
    }


}