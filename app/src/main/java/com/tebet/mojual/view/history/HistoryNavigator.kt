package com.tebet.mojual.view.history

import com.tebet.mojual.data.models.Order
import com.tebet.mojual.data.models.request.SearchOrderRequest
import com.tebet.mojual.view.base.BaseActivityNavigator

interface HistoryNavigator : BaseActivityNavigator {
    fun validate(): Boolean
    fun itemSelected(item: Order)
    fun openOrderStatusPicker()
}
