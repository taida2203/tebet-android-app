package com.tebet.mojual.data.models.request

data class SearchOrderRequest(
    var quantity: Int? = null,
    var planDate: Long? = null,

    var fromAmount: Double? = null,
    var toAmount: Double? = null,

    var fromPlanDate: Long? = null,
    var toPlanDate: Long? = null,

    var fromSaleDate: Long? = null,
    var toSaleDate: Long? = null,

    var status: String? = null,
    var statusList: String? = null,

    var containerStatus: String? = null,
    var containerStatusList: String? = null,

    var loadCustomer: Boolean? = null,
    var loadContainers: Boolean? = null,
    var hasNoContainer: Boolean? = null,
    var profileId: Int? = null,
    var profileCode: String? = null,
    var orderCode: String? = null,
    var orders: Map<String, String>? = mapOf(Pair("od.createdDate", "DESC")),
    var limit: Int? = null,
    var offset: Int? = null
)


//
//var Map<String, String> orders,
