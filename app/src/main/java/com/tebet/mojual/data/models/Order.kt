package com.tebet.mojual.data.models

import java.io.Serializable

data class Order(
    var orderId: String,
    var orderCode: String,
    var quantity: Int?,
    var planDate: Long?,
    var price: Double?,
    var totalPrice: Double?
) : Serializable
