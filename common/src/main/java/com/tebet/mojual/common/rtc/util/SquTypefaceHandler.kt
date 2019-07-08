package com.tebet.mojual.common.rtc.util

import android.content.Context
import android.graphics.Typeface
import com.tebet.mojual.common.rtc.constant.TypefaceConstant
import com.tebet.mojual.common.rtc.view.TypeFactoryGothamRounded

object SquTypefaceHandler {

    private lateinit var typeFactoryGothamRounded: TypeFactoryGothamRounded

    fun initialize(baseContext: Context) {
        typeFactoryGothamRounded = TypeFactoryGothamRounded(baseContext)
    }

    fun getTypeface(type: Int): Typeface {
        return when (type) {
            TypefaceConstant.REGULAR -> typeFactoryGothamRounded.regularTypeFace
            TypefaceConstant.BOLD -> typeFactoryGothamRounded.boldTypeFace
            else -> typeFactoryGothamRounded.regularTypeFace
        }
    }

}