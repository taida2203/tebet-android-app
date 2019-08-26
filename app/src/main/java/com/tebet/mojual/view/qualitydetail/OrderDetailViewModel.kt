package com.tebet.mojual.view.qualitydetail

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.OrderContainer
import com.tebet.mojual.data.models.OrderDetail
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
            it.orderId = 303 // hard code for show data
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
        navigator.openHomeScreen()
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
        navigator.openHomeScreen()
    }
}