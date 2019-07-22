package com.tebet.mojual.common.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import co.common.util.SquTypefaceHandler
import com.tebet.mojual.common.R

class AppTextView : AppCompatTextView {
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
//        val typeArray = context.theme.obtainStyledAttributes(
//            attributeSet,
//            R.styleable.SquTextFont,
//            0,
//            0
//        )
//
//        val typefaceType: Int
//
//        try {
//            typefaceType = typeArray.getInteger(R.styleable.SquTextFont_font_style, 1)
//        } finally {
//            typeArray.recycle()
//        }
//
//        if (!isInEditMode) typeface = SquTypefaceHandler.getTypeface(typefaceType)
    }
}