package com.tebet.mojual.data.models.request

data class SearchOrderRequest(var quantity: Int? = null, var planDate: Long? = null, var limit: Int? = null, var offset: Int? = null)