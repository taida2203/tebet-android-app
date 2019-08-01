package com.tebet.mojual.view.saledetail

import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.view.base.BaseNavigator
import com.tebet.mojual.view.base.BaseViewModel

class SaleDetailViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<SaleDetailNavigator>(dataManager, schedulerProvider) {
    fun onReCreateOrderClick() {
        navigator.openSaleScreen()
    }
}