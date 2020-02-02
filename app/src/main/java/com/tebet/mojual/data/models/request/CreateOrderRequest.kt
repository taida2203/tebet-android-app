package com.tebet.mojual.data.models.request

import com.tebet.mojual.data.models.Quality

data class CreateOrderRequest(
    var quantity: Int?,
    var planDate: Long?,
    var qualityList: List<Quality>? = null,
    var containerType: String? = null
)