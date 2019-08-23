package com.tebet.mojual.common.util

import java.text.DecimalFormat

fun Double.toDisplayMoney(): String {
    if (this <= 0) {
        return "Rp 0"
    }
    val formatter = DecimalFormat("#,###")
    val formattedNumber = formatter.format(this).replace(",", ".")
    return "Rp. $formattedNumber"
}

fun Double.toDisplayWeight(): String {
    return this.toInt().toString()
}