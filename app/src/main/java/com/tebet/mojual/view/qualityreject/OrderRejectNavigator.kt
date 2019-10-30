package com.tebet.mojual.view.qualityreject

import com.tebet.mojual.data.models.OrderDetail
import com.tebet.mojual.view.base.BaseActivityNavigator

interface OrderRejectNavigator : BaseActivityNavigator {
    fun validate(): Boolean
    fun openOrderDetailScreen(order: OrderDetail)
}
