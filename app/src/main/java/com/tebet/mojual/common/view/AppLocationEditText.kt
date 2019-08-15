package com.tebet.mojual.common.view

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.tebet.mojual.R

open class AppLocationEditText : AppEditText {
    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int)
            : super(context, attributeSet, defStyle)

    override fun setFont(attributeSet: AttributeSet?) {
        super.setFont(attributeSet)
        this.setHintTextColor(ContextCompat.getColor(context, R.color.green_dark))
        setSingleLine(false)
        maxLines = 2

        val img = ContextCompat.getDrawable(context, R.drawable.signup_location)
        img?.setBounds(60, 60, 60, 60)
        setCompoundDrawablesWithIntrinsicBounds(null, null, img, null)
    }
}