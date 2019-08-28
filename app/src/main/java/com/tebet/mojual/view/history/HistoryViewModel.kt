package com.tebet.mojual.view.history

import androidx.recyclerview.widget.DiffUtil
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.data.models.Paging
import com.tebet.mojual.data.models.request.SearchOrderRequest
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import me.tatarka.bindingcollectionadapter2.ItemBinding
import me.tatarka.bindingcollectionadapter2.collections.AsyncDiffObservableList

class HistoryViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<HistoryNavigator>(dataManager, schedulerProvider) {
    var itemBinding: ItemBinding<Order> =
        ItemBinding.of<Order>(BR.item, R.layout.item_history)
            .bindExtra(BR.listener, object : OnFutureDateClick {
                override fun onItemClick(item: Order) {
                    navigator.itemSelected(item)
                }
            })
    var items: AsyncDiffObservableList<Order> =
        AsyncDiffObservableList(object : DiffUtil.ItemCallback<Order>() {
            override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
                return oldItem.orderId.equals(newItem.orderId)
            }

            override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
                return oldItem.orderCode.equals(newItem.orderCode)
            }
        })

    fun loadData(page: Int = 0) {
        val offset = page * 10
        if (items.size >= offset) {
            navigator.showLoading(true)
            compositeDisposable.add(
                dataManager.searchOrders(SearchOrderRequest(offset = page * 10, limit = 10))
                    .observeOn(schedulerProvider.ui())
                    .subscribeWith(object : CallbackWrapper<Paging<Order>>() {
                        override fun onSuccess(dataResponse: Paging<Order>) {
                            if (dataResponse.data.isNotEmpty()) {
                                items.update(dataResponse.data)
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
    }

    interface OnFutureDateClick {
        fun onItemClick(item: Order)
    }

    fun onSubmitClick() {
        if (!navigator.validate()) {
            return
        }
    }
}