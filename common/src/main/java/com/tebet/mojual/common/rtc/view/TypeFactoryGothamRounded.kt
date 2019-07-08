package com.tebet.mojual.common.rtc.view

import android.content.Context
import android.graphics.Typeface

class TypeFactoryGothamRounded(context: Context) {

    companion object {
        const val GOTHAM_ROUNDED_REGULAR = "fonts/GothamRoundedBook.ttf"
        const val GOTHAM_ROUNDED_BOLD = "fonts/GothamRoundedBold.ttf"
    }

    val regularTypeFace: Typeface = Typeface.createFromAsset(context.assets, GOTHAM_ROUNDED_REGULAR)
    val boldTypeFace: Typeface = Typeface.createFromAsset(context.assets, GOTHAM_ROUNDED_BOLD)

}