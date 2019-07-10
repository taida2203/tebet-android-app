package co.common.event

/**
 * Created by heo on 4/13/17.
 */

interface NotificationEvent {
    companion object {
        const val FORCE_NAVIGATE_TO = "FORCE_NAVIGATE_TO"
    }

    var message: String?
    var eventCode: String?
    var notyType : co.common.event.NotificationEvent.NOTY_TYPE
    var map: MutableMap<String, Any?>?

    enum class NOTY_TYPE {
        SHOW_ALERT,
        FORWARD_DIRECTLY
    }
}


