package com.tebet.mojual.view.home

import com.tebet.mojual.data.models.Order
import com.tebet.mojual.data.models.OrderContainer
import com.tebet.mojual.data.models.OrderDetail
import com.tebet.mojual.data.models.request.SearchOrderRequest

interface HomeNavigator {
    fun showCheckQualityScreen()
    fun showBorrowScreen()
    fun showTipsScreen()
    fun onBackPressed()
    fun showProfileScreen()
    fun showSellScreen()
    fun showOrderCompleteScreen(dataResponse: Order)
    fun showOrderDetailScreen(dataResponse: Order)
    fun showHomeScreen()
    fun showHistoryScreen()
    fun showAddContainerScreen(dataResponse: Order)
    fun showRejectReasonScreen(order: OrderDetail, selectedItems: List<OrderContainer>)
    fun showInboxScreen()
    fun showHistorySearchScreen(searchOrderRequest: SearchOrderRequest)
    fun showBankConfirmScreen(order: OrderDetail, selectedItems: List<OrderContainer>)
}
