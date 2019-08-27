package com.tebet.mojual.data.models.request

data class SearchOrderRequest(
    var quantity: Int? = null,
    var planDate: Long? = null,
    var status: String? = null,
    var statusList: String? = null,
    var containerStatus: String? = null,
    var containerStatusList: String? = null,
    var loadCustomer: Boolean? = null,
    var loadContainers: Boolean? = null,
    var orders: Map<String, String>? = null,
    var limit: Int? = null,
    var offset: Int? = null
)


//
//private Map<String, String> orders;
