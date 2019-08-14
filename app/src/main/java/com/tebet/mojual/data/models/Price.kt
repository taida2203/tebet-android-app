package com.tebet.mojual.data.models

import java.io.Serializable

data class Price(
    var date: Long,
    var price: Double
) : Serializable {
    var isIncrease: Boolean? = null
}
