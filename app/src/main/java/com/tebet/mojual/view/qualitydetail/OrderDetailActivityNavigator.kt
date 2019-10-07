package com.tebet.mojual.view.qualitydetail

import com.tebet.mojual.data.models.OrderContainer
import com.tebet.mojual.data.models.OrderDetail
import com.tebet.mojual.view.base.BaseActivityNavigator

interface OrderDetailActivityNavigator :BaseActivityNavigator{
    fun openPreviousScreen()
    fun onBankConfirmClick(order: OrderDetail, selectedItems: List<OrderContainer>)
    fun onReasonClick(order: OrderDetail, selectedItems: List<OrderContainer>)
}
