package com.tebet.mojual.view.qualitycheck

import com.tebet.mojual.data.models.Order
import com.tebet.mojual.data.models.Price
import com.tebet.mojual.view.base.BaseActivityNavigator

interface QualityNavigator : BaseActivityNavigator {
    fun validate(): Boolean
    fun itemSelected(item: Order)
    fun openSellScreen()
    fun openAddContainerScreen()
}
