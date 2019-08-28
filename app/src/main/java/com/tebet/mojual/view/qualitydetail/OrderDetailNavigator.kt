package com.tebet.mojual.view.qualitydetail

import com.tebet.mojual.data.models.OrderContainer
import com.tebet.mojual.data.models.OrderDetail
import com.tebet.mojual.view.base.BaseActivityNavigator

interface OrderDetailNavigator : BaseActivityNavigator {
    fun validate(): Boolean
    fun itemSelected(item: OrderContainer)
    fun openHomeScreen()
    fun openRejectReasonScreen(order: OrderDetail, selectedItems: List<OrderContainer>)
    fun showConfirmDialog(selectedItems: List<OrderContainer>)
}
