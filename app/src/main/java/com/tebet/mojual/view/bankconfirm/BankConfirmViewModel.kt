package com.tebet.mojual.view.bankconfirm

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.data.models.OrderContainer
import com.tebet.mojual.data.models.OrderDetail
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel


class BankConfirmViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<BankConfirmNavigator>(dataManager, schedulerProvider) {
    var order: ObservableField<OrderDetail> = ObservableField()
    var items: ObservableArrayList<OrderContainer> = ObservableArrayList()
    var rejectMessage1: ObservableField<String> = ObservableField()
    var rejectMessage2: ObservableField<String> = ObservableField()

    fun onSubmitClick() {
        if (!navigator.validate()) {
            return
        }
        order.get()?.let { od ->
            navigator.showLoading(true)
            compositeDisposable.add(
                dataManager.confirmOrder(od.orderId, items.map {
                    it.rejectMessage1 = rejectMessage1.get()
                    it.rejectMessage2 = rejectMessage2.get()
                    it
                })
                    .observeOn(schedulerProvider.ui())
                    .subscribeWith(object : CallbackWrapper<Order>() {
                        override fun onSuccess(dataResponse: Order) {
                            navigator.showLoading(false)
                            order.get()?.let {
                                navigator.openOrderDetailScreen(it)
                            }
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