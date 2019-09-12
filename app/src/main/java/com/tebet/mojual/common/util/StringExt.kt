package com.tebet.mojual.common.util

import com.tebet.mojual.data.models.enumeration.ContainerOrderState
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
    return this.toString()
}

fun Double.toDisplayPercent(): String {
    return this.toInt().toString() + "%"
}

fun ContainerOrderState.toDisplayContainerStatus(): String {
    return this.name.replace("_", " ").toLowerCase().capitalize()
}