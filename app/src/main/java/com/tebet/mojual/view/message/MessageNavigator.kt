package com.tebet.mojual.view.message

import com.tebet.mojual.data.models.Message
import com.tebet.mojual.view.base.BaseActivityNavigator

interface MessageNavigator : BaseActivityNavigator {
    fun openNotificationDetail(item: Message)
    fun showUnreadCount(unreadCountResponse: Long)
}
