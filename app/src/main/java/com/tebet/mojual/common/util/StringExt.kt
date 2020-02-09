package com.tebet.mojual.common.util

import com.tebet.mojual.R
import com.tebet.mojual.data.models.enumeration.*
import java.text.DecimalFormat
import kotlin.math.roundToInt

fun Double?.toDisplayMoney(): String {
    if (this == null || this <= 0) {
        return "Rp 0"
    }
    val formatter = DecimalFormat("#,###")
    val formattedNumber = formatter.format(this).replace(",", ".")
    return "Rp $formattedNumber"
}

fun Float?.toDisplayMoney(): String {
    return this?.toDouble().toDisplayMoney()
}

fun String?.toDisplayType(): String {
    if (this == null) {
        return "N/A"
    }
    return when (this) {
        ContainerOrderType.JERRYCAN.name -> Utility.getInstance().getString(R.string.container_type_can)
        ContainerOrderType.DRUM.name -> Utility.getInstance().getString(R.string.container_type_drum)
        else -> this?.replace("_", " ")?.toLowerCase()?.capitalize()
    }
}

fun Double?.toDisplayPoint(): String {
    return toDisplayMoney().replace("Rp ", "") + " " + Utility.getInstance().getString(R.string.order_detail_point)
}

fun Float?.toDisplayPoint(): String {
    return this?.toDouble().toDisplayPoint()
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

fun DocumentType?.toDisplayStatus(): String {
    return when (this) {
        DocumentType.LOGISTIC_RECEIPT -> Utility.getInstance().getString(R.string.document_type_logistic_receipt)
        DocumentType.OTHER -> Utility.getInstance().getString(R.string.document_type_other)
        else -> Utility.getInstance().getString(R.string.document_type_other)
    }
}

fun String?.fromStatusCodeToDisplayable(): String? {
    return when (this) {
        OrderStatus.OPEN.name -> Utility.getInstance().getString(R.string.order_status_open)
        OrderStatus.CLOSED.name -> Utility.getInstance().getString(R.string.order_status_closed)
        OrderStatus.REJECTED.name -> Utility.getInstance().getString(R.string.order_status_rejected)
        ContainerOrderState.PAID.name -> Utility.getInstance().getString(R.string.container_status_paid)
        ContainerOrderState.SALE_CONFIRMED.name -> Utility.getInstance().getString(R.string.container_status_sale_confirmed)
        ContainerOrderState.FINAL_PRICE_OFFERED.name -> Utility.getInstance().getString(R.string.container_status_final_price_offered)
        ContainerOrderState.RETURNED.name -> Utility.getInstance().getString(R.string.container_status_returned)
        ContainerOrderState.SALE_REJECTED.name -> Utility.getInstance().getString(R.string.container_status_sale_rejected)
        ContainerOrderState.CHECKING.name -> Utility.getInstance().getString(R.string.container_status_checking)
        ContainerOrderState.SALE_REQUESTED.name -> Utility.getInstance().getString(R.string.container_status_sale_requested)
        ContainerOrderState.INITIAL_PRICE_REJECTED.name -> Utility.getInstance().getString(R.string.container_status_initial_price_rejected)
        ContainerOrderState.QUALITY_REJECTED.name -> Utility.getInstance().getString(R.string.container_status_quality_rejected)
        ContainerOrderState.INITIAL_PRICE_OFFERED.name -> Utility.getInstance().getString(R.string.container_status_initial_price_offered)
        else -> this?.replace("_", " ")?.toLowerCase()?.capitalize()
    }
}

fun Long?.toDisplayLong(): String? {
    return this?.toString()
}