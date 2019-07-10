package com.tebet.mojual.notification

import com.tebet.mojual.data.remote.NotificationNewResponse

class NotificationTitle(title: String) : NotificationNewResponse("") {
    init {
        this.title = title
    }
}
