package com.tebet.mojual.common.rtc.event

import androidx.collection.ArrayMap
import com.tebet.mojual.common.rtc.model.Notification

/**
 * Created by heo on 4/13/17.
 */

interface NotificationEvent {
    companion object {
        const val FORCE_NAVIGATE_TO = "FORCE_NAVIGATE_TO"
    }

    var message: String?
    var eventCode: String?
    var notyType : NOTY_TYPE
    var map: MutableMap<String, Any?>?

    enum class NOTY_TYPE {
        SHOW_ALERT,
        FORWARD_DIRECTLY
    }
}


