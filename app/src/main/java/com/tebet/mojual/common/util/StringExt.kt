package com.tebet.mojual.common.util

fun Double.toDisplayMoney(): String {
    if (this <= 0) {
        return "Rp. 0"
    }
    return "Rp. ${this.toInt()}"
}
