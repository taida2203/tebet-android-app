package com.tebet.mojual.common.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.tebet.mojual.R

open class AppEditText : AppCompatEditText {
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
        this.setPadding(20, 20, 20, 20)
        this.setTextColor(ContextCompat.getColor(context, R.color.grey))
        this.setHintTextColor(ContextCompat.getColor(context, R.color.grey))
        setLines(1)
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