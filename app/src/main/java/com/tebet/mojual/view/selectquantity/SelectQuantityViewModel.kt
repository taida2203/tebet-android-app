package com.tebet.mojual.view.selectquantity

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.view.base.BaseViewModel
import com.tebet.mojual.view.selectfuturedate.SelectFutureDateViewModel
import me.tatarka.bindingcollectionadapter2.ItemBinding

class SelectQuantityViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<SelectQuantityNavigator>(dataManager, schedulerProvider) {
    var items: ObservableList<String> = ObservableArrayList()
    var itemBinding: ItemBinding<String> = ItemBinding.of<String>(BR.item, R.layout.item_select_quantity)
        .bindExtra(BR.listener, object : OnQuantityClick {
            override fun onItemClick(item: String) {
                navigator.itemSelected(item)
            }
        })

    fun loadData() {
        val quantity = arrayListOf(1, 2, 3, 4, 5, 6, 7)
        quantity.forEach { item ->
            items.add(item.toString())
        }
    }

    interface OnQuantityClick {
        fun onItemClick(item: String)
    }
}