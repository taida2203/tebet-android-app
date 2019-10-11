package com.tebet.mojual.common.util

import com.tebet.mojual.R
import com.tebet.mojual.data.models.enumeration.OrderStatus
import com.tebet.mojual.data.models.enumeration.SortBy
import com.tebet.mojual.data.models.enumeration.SortType
import java.text.DecimalFormat
import kotlin.math.roundToInt

fun Double?.toDisplayMoney(): String {
    if (this == null || this <= 0) {
        return "Rp 0"
    }
    val formatter = DecimalFormat("#,###")
    val formattedNumber = formatter.format(this).replace(",", ".")
    return "Rp. $formattedNumber"
}

fun Float?.toDisplayMoney(): String {
    return this?.toDouble().toDisplayMoney()
}

fun Double?.toDisplayWeight(): String {
    if (this == null || this <= 0) {
        return "0Kg"
    }
    val decimalFormat =  DecimalFormat("0.###")
    return decimalFormat.format(this) + "Kg"
}

fun Double?.toDisplayPercent(): String {
    if (this == null || this <= 0) {
        return "0%"
    }
    return ((this * 100).roundToInt() / 100f).toString().replace("\\.0+$", "") + "%"
}

fun OrderStatus?.toDisplayStatus(): String {
    return when (this) {
        OrderStatus.REJECTED -> Utility.getInstance().getString(R.string.history_advance_status_rejected)
        OrderStatus.CLOSED -> Utility.getInstance().getString(R.string.history_advance_status_closed)
        OrderStatus.OPEN -> Utility.getInstance().getString(R.string.history_advance_status_open)
        else -> Utility.getInstance().getString(R.string.history_advance_status_all)
    }
}

fun SortBy?.toDisplayStatus(): String {
    return when (this) {
        SortBy.ORDER_ID -> Utility.getInstance().getString(R.string.history_advance_status_order_id)
        SortBy.STATUS -> Utility.getInstance().getString(R.string.history_advance_status_status)
        SortBy.SALE_DATE -> Utility.getInstance().getString(R.string.history_advance_status_sale_date)
        SortBy.AMOUNT -> Utility.getInstance().getString(R.string.history_advance_status_amount)
        else -> Utility.getInstance().getString(R.string.history_advance_status_sale_date)
    }
}

fun SortType?.toDisplayStatus(): String {
    return when (this) {
        SortType.DESC -> Utility.getInstance().getString(R.string.history_advance_status_desc)
        SortType.ASC -> Utility.getInstance().getString(R.string.history_advance_status_asc)
        else -> Utility.getInstance().getString(R.string.history_advance_status_desc)
    }
}

fun String?.fromStatusCodeToDisplayable(): String? {
    return this?.replace("_", " ")?.toLowerCase()?.capitalize()
}

fun String?.fromCodeToDisplayable(): String? {
    return this?.replace("_", " ")
}

fun Long?.toDisplayLong(): String? {
    return this?.toString()
}