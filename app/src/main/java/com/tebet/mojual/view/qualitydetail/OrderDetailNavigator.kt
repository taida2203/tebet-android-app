package com.tebet.mojual.view.qualitydetail

import com.tebet.mojual.data.models.IOrder
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.data.models.OrderContainer
import com.tebet.mojual.view.base.BaseActivityNavigator

interface OrderDetailNavigator : BaseActivityNavigator {
    fun validate(): Boolean
    fun itemSelected(item: OrderContainer)
    fun openSellScreen()
    fun openAddContainerScreen(selectedItem: IOrder)
}
