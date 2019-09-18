package com.tebet.mojual.view.selectfuturedate

import androidx.databinding.ObservableArrayList
import androidx.databinding.library.baseAdapters.BR
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

    override fun loadData(isForceLoad: Boolean?) {
        navigator.showLoading(true)
        compositeDisposable.add(
            dataManager.getNext7DaysPrice()
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : CallbackWrapper<List<Price>>() {
                    override fun onSuccess(dataResponse: List<Price>) {
                        navigator.showLoading(false)
                        if (dataResponse.isEmpty()) return
                        dataResponse[0].isToday = true
                        dataResponse.map { responsePrice ->
                            responsePrice.isIncrease = when (dataResponse.minBy { price -> price.date }?.pricePerContainer?.compareTo(responsePrice.pricePerContainer)) {
                                -1 -> true
                                1 -> false
                                else -> null
                            }
                            responsePrice
                        }.forEach { item ->
                            items.add(item)
                        }
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