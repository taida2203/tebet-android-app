package com.tebet.mojual.common.rtc.network

class JsonWrapper<T> (
    var status: String? = null,
    var messages: List<String>? = null,
    var data: T? = null
)