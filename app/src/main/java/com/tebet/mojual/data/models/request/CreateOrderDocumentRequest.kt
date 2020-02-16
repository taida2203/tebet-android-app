package com.tebet.mojual.data.models.request

data class CreateOrderDocumentRequest(
    var filePath: String,
    var type: String,
    var description: String? = null
)