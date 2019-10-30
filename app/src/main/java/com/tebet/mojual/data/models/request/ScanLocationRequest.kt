package com.tebet.mojual.data.models.request

data class ScanLocationRequest(
    var deviceId: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var orderCode: String? = null,
    var orderId: Long? = null,
    var scanEvent: String? = null
)
