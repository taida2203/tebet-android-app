package com.tebet.mojual.view.qualitycheck

import androidx.databinding.ObservableArrayList
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.data.models.Paging
import com.tebet.mojual.data.models.Price
import com.tebet.mojual.data.models.request.SearchOrderRequest
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import me.tatarka.bindingcollectionadapter2.ItemBinding


class QualityViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<QualityNavigator>(dataManager, schedulerProvider) {
    var items: ObservableArrayList<Order> = ObservableArrayList()
    var itemBinding: ItemBinding<Order> =
        ItemBinding.of<Order>(BR.item, R.layout.item_quality_check_order)
            .bindExtra(BR.listener, object : OnFutureDateClick {
                override fun onItemClick(item: Order) {
                    navigator.itemSelected(item)
                }
            })

    fun loadData() {
        navigator.showLoading(true)
        compositeDisposable.add(
            dataManager.searchOrders(SearchOrderRequest())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : CallbackWrapper<Paging<Order>>() {
                    override fun onSuccess(dataResponse: Paging<Order>) {
                        dataResponse.data.forEach { order ->
                            items.add(order)
                        }
                        navigator.showLoading(false)
                    }

                    override fun onFailure(error: String?) {
                        navigator.showLoading(false)
                        handleError(error)
                    }
                })
        )
    }

    interface OnFutureDateClick {
        fun onItemClick(item: Order)
    }

    fun onSubmitClick() {
        if (!navigator.validate()) {
            return
        }
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