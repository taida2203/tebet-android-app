package com.tebet.mojual.data.models

import java.io.Serializable

data class Price(
    var date: Long,
    var pricePerContainer: Double,
    var pricePerKg: Double
) : Serializable {
    var isIncrease: Boolean? = null
    var isToday: Boolean? = null
}
