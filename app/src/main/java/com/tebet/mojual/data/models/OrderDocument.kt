package com.tebet.mojual.data.models

import java.io.Serializable

data class OrderDocument(
    val orderDocumentId: Long,
    val orderId: Long,
    val orderCode: String,
    val filePath: String,
    val type: String,
    val description: String,
    val createdDate: Long
) : Serializable