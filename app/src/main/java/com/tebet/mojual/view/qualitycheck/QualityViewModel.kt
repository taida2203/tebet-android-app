package com.tebet.mojual.view.qualitycheck

import androidx.databinding.ObservableArrayList
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.data.models.Paging
import com.tebet.mojual.data.models.request.SearchOrderRequest
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import me.tatarka.bindingcollectionadapter2.OnItemBind


class QualityViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<QualityNavigator>(dataManager, schedulerProvider) {
    var items: ObservableArrayList<Order> = ObservableArrayList()
    val onItemBind: OnItemBind<Order> = OnItemBind { itemBinding, position, item ->
        itemBinding.set(BR.item, if (position == items.size - 1) R.layout.item_quality_check_order_add else R.layout.item_quality_check_order)
        itemBinding.bindExtra(BR.listener, object : OnFutureDateClick {
            override fun onItemClick(item: Order) = if (item.orderId >= 0) {
                item.isSelected = true
                items.filterNot { adapterItem -> adapterItem.orderId == item.orderId }.forEach { it.isSelected = false }
                navigator.itemSelected(item)
            } else navigator.openSellScreen()
        })
    }
    var footerItem = Order(-1, "")
//    var itemBinding: ItemBinding<Order> =
//        ItemBinding.of<Order>(BR.item, R.layout.item_quality_check_order)
//            .bindExtra(BR.listener, object : OnFutureDateClick {
//                override fun onItemClick(item: Order) {
//                    navigator.itemSelected(item)
//                }
//            })

    fun loadData(page: Int = 0) {
        val offset = page * 10
        if (items.size >= offset) {
            navigator.showLoading(true)
            compositeDisposable.add(
                dataManager.searchOrders(SearchOrderRequest(offset = offset, limit = 10))
                    .observeOn(schedulerProvider.ui())
                    .subscribeWith(object : CallbackWrapper<Paging<Order>>() {
                        override fun onSuccess(dataResponse: Paging<Order>) {
                            items.remove(footerItem)
                            items.addAll(dataResponse.data)
                            items.add(footerItem)
                            navigator.showLoading(false)
                        }

                        override fun onFailure(error: String?) {
                            navigator.showLoading(false)
                            handleError(error)
                            items.clear()
                            items.add(Order(-1, ""))
                        }
                    })
            )
        }
    }

    interface OnFutureDateClick {
        fun onItemClick(item: Order)
    }

    fun onSubmitClick() {
        if (!navigator.validate()) {
            return
        }
        var selectedItem = items.firstOrNull { item -> item.isSelected }
        if (selectedItem == null) {
            navigator.show(R.string.quality_check_error_select_order)
            return
        }
        navigator.openAddContainerScreen(selectedItem)
//        navigator.showLoading(true)
//        compositeDisposable.add(
//            dataManager.createOrder(
//                CreateOrderRequest(
//                    selectedQuantity.get(),
//                    selectedFutureDate.get()?.date
//                )
//            )
//                .observeOn(schedulerProvider.ui())
//                .subscribeWith(object : CallbackWrapper<Order>() {
//                    override fun onSuccess(dataResponse: Order) {
//                        dataResponse.price = selectedFutureDate.get()?.price
//                        dataResponse.totalPrice = simulationPrice.get()
//                        navigator.showLoading(false)
//                        navigator.openSaleScreen(dataResponse)
//                    }
//
//                    override fun onFailure(error: String?) {
//                        navigator.showLoading(false)
//                        handleError(error)
//                    }
//                })
//        )
    }
}