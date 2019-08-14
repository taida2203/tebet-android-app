package com.tebet.mojual.view.selectfuturedate

import androidx.databinding.ObservableArrayList
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.Price
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import me.tatarka.bindingcollectionadapter2.ItemBinding

class SelectFutureDateViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<SelectFutureDateNavigator>(dataManager, schedulerProvider) {
    var items: ObservableArrayList<Price> = ObservableArrayList()
    var itemBinding: ItemBinding<Price> =
        ItemBinding.of<Price>(BR.item, R.layout.item_select_future_date)
            .bindExtra(BR.listener, object : OnFutureDateClick {
                override fun onItemClick(item: Price) {
                    navigator.itemSelected(item)
                }
            })

    fun loadData() {
        navigator.showLoading(true)
        compositeDisposable.add(
            dataManager.getNext7DaysPrice()
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : CallbackWrapper<List<Price>>() {
                    override fun onSuccess(dataResponse: List<Price>) {
                        dataResponse.map { responsePrice ->
                            responsePrice.isIncrease = when (dataResponse.minBy { price -> price.date }?.price?.compareTo(responsePrice.price)) {
                                -1 -> true
                                1 -> false
                                else -> null
                            }
                            responsePrice
                        }.forEach { item ->
                            items.add(item)
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
        fun onItemClick(item: Price)
    }
}