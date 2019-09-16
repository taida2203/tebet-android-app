package com.tebet.mojual.common.util

import com.tebet.mojual.data.models.enumeration.ContainerOrderState
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
    return decimalFormat.format(this)
}

fun Double.toDisplayPercent(): String {
    return ((this * 100).roundToInt() / 100f).toString().replace("\\.0+$", "") + "%"
}

fun ContainerOrderState.toDisplayContainerStatus(): String {
    return this.name.replace("_", " ").toLowerCase().capitalize()
}