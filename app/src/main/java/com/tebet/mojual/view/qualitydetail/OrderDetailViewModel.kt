package com.tebet.mojual.view.qualitydetail

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.data.models.OrderContainer
import com.tebet.mojual.data.models.OrderDetail
import com.tebet.mojual.data.models.enumeration.AssetAction
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import me.tatarka.bindingcollectionadapter2.ItemBinding


class OrderDetailViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<OrderDetailNavigator>(dataManager, schedulerProvider) {
    var order: ObservableField<OrderDetail> = ObservableField()
    var items: ObservableArrayList<OrderContainer> = ObservableArrayList()
    var itemBinding: ItemBinding<OrderContainer> =
        ItemBinding.of<OrderContainer>(BR.item, R.layout.item_order_detail)
            .bindExtra(BR.listener, object : OnFutureDateClick {
                override fun onItemExpandClick(item: OrderContainer) {
                    item.expanded = !item.expanded
                }

                override fun onItemClick(item: OrderContainer) {
                    item.isSelected = !item.isSelected
                    navigator.itemSelected(item)
                }
            })

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

    interface OnFutureDateClick {
        fun onItemClick(item: OrderContainer)
        fun onItemExpandClick(item: OrderContainer)
    }

    fun onRejectClick() {
        if (!navigator.validate()) {
            return
        }
        order.get()?.let { od ->
            navigator.openRejectReasonScreen(od, items.toList().map {
                it.action = AssetAction.REJECT.name
                it
            })
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
        navigator.showConfirmDialog(selectedItems.map {
            it.action = AssetAction.APPROVE.name
            it
        })
    }

    fun submitOrder(selectedItems: List<OrderContainer>) {
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