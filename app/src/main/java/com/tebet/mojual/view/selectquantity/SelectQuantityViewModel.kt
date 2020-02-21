package com.tebet.mojual.view.selectquantity

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.databinding.library.baseAdapters.BR
import com.tebet.mojual.R
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.Asset
import com.tebet.mojual.data.models.NetworkError
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import me.tatarka.bindingcollectionadapter2.ItemBinding

class SelectQuantityViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<SelectQuantityNavigator>(dataManager, schedulerProvider) {
    var containerType: String? = null
    var items: ObservableList<String> = ObservableArrayList()
    var itemBinding: ItemBinding<String> = ItemBinding.of<String>(BR.item, R.layout.item_select_quantity)
        .bindExtra(BR.listener, object : OnQuantityClick {
            override fun onItemClick(item: String) {
                navigator.itemSelected(item)
            }
        })

    override fun loadData(isForceLoad: Boolean?) {
        navigator.showLoading(true)
        compositeDisposable.add(
            dataManager.getAssetDB()
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : CallbackWrapper<List<Asset>>() {
                    override fun onSuccess(dataResponse: List<Asset>) {
                        navigator.showLoading(false)
                        var filterData = dataResponse
                        containerType?.let {
                            filterData = dataResponse.filter { it.containerType == containerType }
                        }

                        for (i in 1..filterData.size) {
                            items.add(i.toString())
                        }
                    }

                    override fun onFailure(error: NetworkError) {
                        navigator.showLoading(false)
                        handleError(error)
                    }
                })
        )
    }

    interface OnQuantityClick {
        fun onItemClick(item: String)
    }
}