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
import me.tatarka.bindingcollectionadapter2.collections.MergeObservableList
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass


class QualityViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<QualityNavigator>(dataManager, schedulerProvider) {
    var items: ObservableArrayList<Order> = ObservableArrayList()

    /**
     * Items merged with a header on top
     */
    var headerFooterItems: MergeObservableList<Any> = MergeObservableList<Any>()
        .insertItem("Header")
        .insertList(items)

    val multipleItems: OnItemBindClass<Any> = OnItemBindClass<Any>()
        .map(String::class.java) { itemBinding, position, item ->
            itemBinding.set(BR.item, R.layout.item_quality_check_order_add)
            itemBinding.bindExtra(BR.listener, object : OnListItemClick<String> {
                override fun onItemClick(item: String) = navigator.openSellScreen()
            })
        }
        .map(Order::class.java) { itemBinding, position, item ->
            itemBinding.set(BR.item, R.layout.item_quality_check_order)
            itemBinding.bindExtra(BR.listener, object : OnListItemClick<Order> {
                override fun onItemClick(item: Order) {
                    item.isSelected = true
                    items.filterNot { adapterItem -> adapterItem.orderId == item.orderId }
                        .forEach { it.isSelected = false }
                    item.let { navigator.itemSelected(it) }
                }
            })
        }

    fun loadData(page: Int = 0) {
        val offset = page * 10
        navigator.showLoading(true)
        compositeDisposable.add(
            dataManager.searchOrders(SearchOrderRequest(offset = offset, limit = 10))
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : CallbackWrapper<Paging<Order>>() {
                    override fun onSuccess(dataResponse: Paging<Order>) {
                        items.addAll(dataResponse.data)
                        navigator.showLoading(false)
                    }

                    override fun onFailure(error: String?) {
                        navigator.showLoading(false)
                        handleError(error)
                    }
                })
        )
    }

    interface OnListItemClick<T> {
        fun onItemClick(item: T)
    }

    fun onSubmitClick() {
        if (!navigator.validate()) {
            return
        }
        val selectedItem = items.firstOrNull { item -> item.isSelected }
        if (selectedItem == null) {
            navigator.show(R.string.quality_check_error_select_order)
            return
        }
        navigator.openAddContainerScreen(selectedItem)
    }
}