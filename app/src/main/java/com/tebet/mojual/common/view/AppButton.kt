package com.tebet.mojual.common.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.tebet.mojual.R

class AppButton : AppCompatButton {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        background = ContextCompat.getDrawable(context, R.drawable.rounded_bg_button)
        setTextColor(ContextCompat.getColor(context, R.color.white))
        isAllCaps = true
    }
}
