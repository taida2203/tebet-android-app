package com.tebet.mojual.data.models.request

data class MessageRequest(
    var profileId: Int? = null,
    var limit: Int? = null,
    var offset: Int? = null
)
