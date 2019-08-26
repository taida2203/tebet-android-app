package com.tebet.mojual.view.home

import com.tebet.mojual.data.models.Order

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
}
