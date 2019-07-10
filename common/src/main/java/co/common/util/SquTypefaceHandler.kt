package co.common.util

import android.content.Context
import android.graphics.Typeface
import co.common.constant.TypefaceConstant
import co.common.view.TypeFactoryGothamRounded

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