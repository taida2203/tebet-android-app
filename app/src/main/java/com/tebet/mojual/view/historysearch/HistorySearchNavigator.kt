package com.tebet.mojual.view.historysearch

import com.tebet.mojual.data.models.request.SearchOrderRequest
import com.tebet.mojual.view.base.BaseActivityNavigator

interface HistorySearchNavigator : BaseActivityNavigator {
    fun openHistoryScreen(searchOrderRequest: SearchOrderRequest)
    fun openFromDatePicker()
    fun openToDatePicker()
    fun openOrderStatusPicker()
    fun openOrderByPicker()
    fun openOrderTypePicker()
}
