package com.tebet.mojual.view.qualitycheck

import androidx.databinding.ObservableArrayList
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.common.adapter.OnListItemClick
import com.tebet.mojual.common.util.Utility
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.data.models.Paging
import com.tebet.mojual.data.models.enumeration.ContainerOrderStatus
import com.tebet.mojual.data.models.enumeration.OrderStatus
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
        .insertItem(Utility.getInstance().getString(R.string.check_quality_add_new_order))
        .insertList(items)

    val multipleItemsBind: OnItemBindClass<Any> = OnItemBindClass<Any>()
        .map(String::class.java) { itemBinding, position, item ->
            itemBinding.set(BR.item, R.layout.item_quality_add)
            itemBinding.bindExtra(BR.listener, object : OnListItemClick<String> {
                override fun onItemClick(item: String) = navigator.openSellScreen()
            })
        }
        .map(Integer::class.java, BR.item, R.layout.item_quality_loading)
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
//        headerFooterItems.insertItem(R.layout.item_quality_loading)
        compositeDisposable.add(
            dataManager.searchOrders(SearchOrderRequest(
                hasNoContainer = true,
                status = OrderStatus.OPEN.name,
                offset = (page - 1) * 10,
                limit = 10
            ))
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : CallbackWrapper<Paging<Order>>() {
                    override fun onSuccess(dataResponse: Paging<Order>) {
                        items.addAll(dataResponse.data)
                        navigator.showLoading(false)
//                        headerFooterItems.removeItem(R.layout.item_quality_loading)
                    }

                    override fun onFailure(error: String?) {
                        navigator.showLoading(false)
//                        headerFooterItems.removeItem(R.layout.item_quality_loading)
                        handleError(error)
                    }
                })
        )
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

    fun onTipsClick() {

    }
}