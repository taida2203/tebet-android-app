package com.tebet.mojual.common.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.core.content.ContextCompat
import com.tebet.mojual.R
import com.tebet.mojual.common.util.Utility


open class AppSpinner : androidx.appcompat.widget.AppCompatSpinner {
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
//        this.background = ContextCompat.getDrawable(context, R.drawable.border_edit_text)
//        this.setPadding(Utility.dpToPx(20), Utility.dpToPx(8), Utility.dpToPx(20), Utility.dpToPx(8))
        val img = ContextCompat.getDrawable(context, R.drawable.signup_dropdown_layer)
        img?.setBounds( 60, 60, 60, 60 )
//        setCompoundDrawablesWithIntrinsicBounds(null, null, img, null)
    }


}