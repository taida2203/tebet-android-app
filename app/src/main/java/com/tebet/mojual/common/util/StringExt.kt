package com.tebet.mojual.common.util

import java.text.DecimalFormat

fun Double.toDisplayMoney(): String {
    if (this <= 0) {
        return "Rp 0"
    }
    val formatter = DecimalFormat("#,###")
    val formattedNumber = formatter.format(this)
    return "Rp $formattedNumber"
}
