package com.tebet.mojual.view.home

import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.view.base.BaseViewModel

class HomeViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<HomeNavigator>(dataManager, schedulerProvider) {
    fun onProfileClick() {
        navigator.showProfileScreen()
    }

    fun onSellClick() {
        navigator.showSellScreen()
    }

    fun onSubmitOrderClick() {
        navigator.showOrderDetailScreen()
    }

    fun onHomeClick() {
        navigator.showHomeScreen()
    }
}