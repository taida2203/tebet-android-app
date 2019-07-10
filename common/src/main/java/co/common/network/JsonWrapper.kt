package co.common.network

class JsonWrapper<T> (
    var status: String? = null,
    var messages: List<String>? = null,
    var data: T? = null
)