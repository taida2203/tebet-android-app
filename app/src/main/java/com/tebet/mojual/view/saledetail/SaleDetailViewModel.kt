package com.tebet.mojual.view.saledetail

import androidx.databinding.ObservableField
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.view.base.BaseNavigator
import com.tebet.mojual.view.base.BaseViewModel

class SaleDetailViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<SaleDetailNavigator>(dataManager, schedulerProvider) {
    var order: ObservableField<Order> = ObservableField()

    fun onReCreateOrderClick() {
        navigator.openSaleScreen()
    }
}