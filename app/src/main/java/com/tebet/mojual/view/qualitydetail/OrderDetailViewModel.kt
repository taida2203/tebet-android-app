package com.tebet.mojual.view.qualitydetail

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableDouble
import androidx.databinding.ObservableField
import androidx.databinding.ObservableFloat
import androidx.databinding.library.baseAdapters.BR
import com.tebet.mojual.R
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.NetworkError
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.data.models.OrderContainer
import com.tebet.mojual.data.models.OrderDetail
import com.tebet.mojual.data.models.enumeration.AssetAction
import com.tebet.mojual.data.models.enumeration.ContainerOrderStatus
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import me.tatarka.bindingcollectionadapter2.ItemBinding


class OrderDetailViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<OrderDetailNavigator>(dataManager, schedulerProvider) {
    var totalPrice: ObservableDouble = ObservableDouble()
    var totalBonus: ObservableDouble = ObservableDouble()
    var totalDelivery: ObservableDouble = ObservableDouble()
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
                    updateTotalPrice()
                    navigator.itemSelected(item)
                }
            })

    private fun updateTotalPrice() {
        when {
            order.get()?.canAction == true -> {
                totalPrice.set(items.filter { it.isSelected }.sumByDouble {
                    it.priceTotalDisplay ?: 0.0
                })
                totalBonus.set(items.filter { it.isSelected }.sumByDouble {
                    it.totalVolumeBonus ?: 0.0
                })
                totalDelivery.set(if (items.firstOrNull { it.isSelected } != null) order.get()?.deliveryBonus
                    ?: 0.0 else 0.0)
            }
            else -> {
                order.get()?.totalPrice?.let { totalPrice.set(it) }
                order.get()?.bonus?.let { totalBonus.set(it) }
                order.get()?.deliveryBonus?.let { totalDelivery.set(it) }
            }
        }
    }

    override fun loadData(isForceLoad: Boolean?) {
        order.get()?.let {
            navigator.showLoading(true)
            compositeDisposable.add(
                dataManager.getOrderDetail(it.orderId, loadContainers = true, loadCustomer = true)
                    .observeOn(schedulerProvider.ui())
                    .subscribeWith(object : CallbackWrapper<OrderDetail>() {
                        override fun onSuccess(dataResponse: OrderDetail) {
                            order.set(dataResponse)
                            dataResponse.containers?.forEach { container ->
                                if(container.isRejected) container.isSelected = false
                                if (!items.contains(container)) items.add(container)
                                else items[items.indexOf(container)] = container
                            }
                            updateTotalPrice()
                            navigator.showLoading(false)
                        }

                        override fun onFailure(error: NetworkError) {
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
        navigator.showRejectConfirm()
    }

    fun rejectOrder() {
        val rejectItems = items.toList().map {
            it.action = AssetAction.REJECT.name
            it
        }
        if (rejectItems.firstOrNull { it.status?.equals(ContainerOrderStatus.FIRST_FINALIZED_PRICE_OFFERED.name) == true } != null) {
            order.get()?.let { navigator.openReasonScreen(it, rejectItems) }
        } else {
            submitOrder(rejectItems)
        }
    }

    fun onSubmitClick() {
        if (!navigator.validate()) {
            return
        }
        if (items.firstOrNull { item -> item.isSelected } == null) {
            navigator.show(R.string.quality_check_error_select_order)
            return
        }
        navigator.showConfirmDialog(items)
    }

    private fun submitOrder(selectedItems: List<OrderContainer>) {
        order.get()?.let { order ->
            navigator.showLoading(true)
            compositeDisposable.add(
                dataManager.confirmOrder(order.orderId, selectedItems)
                    .observeOn(schedulerProvider.ui())
                    .subscribeWith(object : CallbackWrapper<Order>() {
                        override fun onSuccess(dataResponse: Order) {
                            navigator.showLoading(false)
                            this@OrderDetailViewModel.order.get()?.let {
                                navigator.openOrderDetailScreen(it)
                            }
                        }

                        override fun onFailure(error: NetworkError) {
                            navigator.showLoading(false)
                            handleError(error)
                        }
                    })
            )
        }
    }

    fun approveOrder(selectedItems: List<OrderContainer>) {
        if (selectedItems.firstOrNull {
                it.status?.equals(ContainerOrderStatus.FIRST_FINALIZED_PRICE_OFFERED.name) == true ||
                        it.status?.equals(ContainerOrderStatus.SECOND_FINALIZED_PRICE_OFFERED.name) == true
            } != null) {
            order.get()?.let { order ->
                navigator.openBankConfirmScreen(order, selectedItems)
            }
        } else {
            submitOrder(selectedItems)
        }
    }
}