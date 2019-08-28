package com.tebet.mojual.view.qualityreject

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import com.tebet.mojual.R
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.data.models.OrderContainer
import com.tebet.mojual.data.models.OrderDetail
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel


class OrderRejectViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<OrderRejectNavigator>(dataManager, schedulerProvider) {
    var order: ObservableField<OrderDetail> = ObservableField()
    var items: ObservableArrayList<OrderContainer> = ObservableArrayList()

    fun loadData() {
        order.get()?.let {
            navigator.showLoading(true)
            compositeDisposable.add(
                dataManager.getOrderDetail(it.orderId, loadContainers = true, loadCustomer = true)
                    .observeOn(schedulerProvider.ui())
                    .subscribeWith(object : CallbackWrapper<OrderDetail>() {
                        override fun onSuccess(dataResponse: OrderDetail) {
                            order.set(dataResponse)
                            dataResponse.containers?.let { it1 -> items.addAll(it1) }
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

    fun onSubmitClick() {
        if (!navigator.validate()) {
            return
        }
        val selectedItems = items.filter { item -> item.isSelected }
        if (selectedItems.isEmpty()) {
            navigator.show(R.string.quality_check_error_select_order)
            return
        }
        submitOrder(selectedItems)
    }

    private fun submitOrder(selectedItems: List<OrderContainer>) {
        order.get()?.let {
            navigator.showLoading(true)
            compositeDisposable.add(
                dataManager.confirmOrder(it.orderId, selectedItems)
                    .observeOn(schedulerProvider.ui())
                    .subscribeWith(object : CallbackWrapper<Order>() {
                        override fun onSuccess(dataResponse: Order) {
                            navigator.showLoading(false)
                            navigator.openHomeScreen()
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