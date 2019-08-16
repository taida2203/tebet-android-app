package com.tebet.mojual.data.models

import java.io.Serializable

data class Order(
    var orderId: Int,
    var orderCode: String,
    var quantity: Int? = null,
    var planDate: Long? = null,
    var price: Double? = null,
    var totalPrice: Double? = null
) : Serializable {
    var isSelected: Boolean = false
}
