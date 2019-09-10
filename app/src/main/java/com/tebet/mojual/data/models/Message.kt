package com.tebet.mojual.data.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.room.Ignore
import java.io.Serializable

data class Message(
    var notificationHistoryId: Long? = null,
    var profileId: Long? = null,
    var profileCode: Long? = null,
    var message: String? = null,
    var createdDate: Long? = null
) : Serializable, BaseObservable() {
}
