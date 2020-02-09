package com.tebet.mojual.data.models.request

data class CreateOrderDocumentRequest(
    var orderId: Long,
    var filePath: String,
    var type: String,
    var description: String? = null
)