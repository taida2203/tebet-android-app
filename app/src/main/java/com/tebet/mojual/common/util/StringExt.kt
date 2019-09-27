package com.tebet.mojual.common.util

import java.text.DecimalFormat
import kotlin.math.roundToInt

fun Double.toDisplayMoney(): String {
    if (this <= 0) {
        return "Rp 0"
    }
    val formatter = DecimalFormat("#,###")
    val formattedNumber = formatter.format(this).replace(",", ".")
    return "Rp. $formattedNumber"
}

fun Float.toDisplayMoney(): String {
    return this.toDouble().toDisplayMoney()
}

fun Double.toDisplayWeight(): String {
    val decimalFormat =  DecimalFormat("0.###")
    return decimalFormat.format(this) + "Kg"
}

fun Double.toDisplayPercent(): String {
    return ((this * 100).roundToInt() / 100f).toString().replace("\\.0+$", "") + "%"
}

fun String?.fromStatusCodeToDisplayable(): String? {
    return this?.replace("_", " ")?.toLowerCase()?.capitalize()
}
fun String?.fromCodeToDisplayable(): String? {
    return this?.replace("_", " ")
}