package com.tebet.mojual.view.saledetail

import androidx.databinding.ObservableDouble
import androidx.databinding.ObservableField
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.Asset
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.data.models.OrderDetail
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseNavigator
import com.tebet.mojual.view.base.BaseViewModel

class SaleDetailViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<SaleDetailNavigator>(dataManager, schedulerProvider) {
    var order: ObservableField<OrderDetail> = ObservableField()
    var price: ObservableDouble = ObservableDouble(0.0)

    fun onReCreateOrderClick() {
        navigator.openSaleScreen()
    }

    fun loadData() {
        order.get()?.let {
            navigator.showLoading(true)
            it.price?.let { it1 -> price.set(it1) }
            compositeDisposable.add(
                dataManager.getOrderDetail(it.orderId)
                    .observeOn(schedulerProvider.ui())
                    .subscribeWith(object : CallbackWrapper<OrderDetail>() {
                        override fun onSuccess(dataResponse: OrderDetail) {
                            order.set(dataResponse)
                            navigator.showLoading(false)
                        }

                        override fun onFailure(error: String?) {
                            navigator.showLoading(false)
                            handleError(error)
                        }
                    })
            )
        }
    }
}