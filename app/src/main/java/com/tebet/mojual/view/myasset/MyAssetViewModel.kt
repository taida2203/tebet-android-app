package com.tebet.mojual.view.myasset

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.databinding.library.baseAdapters.BR
import com.tebet.mojual.R
import com.tebet.mojual.common.adapter.OnListItemClick
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.Asset
import com.tebet.mojual.data.models.NetworkError
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import me.tatarka.bindingcollectionadapter2.ItemBinding

class MyAssetViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<MyAssetNavigator>(dataManager, schedulerProvider) {
    var items: ObservableList<Asset> = ObservableArrayList()
    var itemBinding: ItemBinding<Asset> = ItemBinding.of<Asset>(BR.item, R.layout.item_my_asset)
        .bindExtra(BR.listener,
            object : OnListItemClick<Asset> {
                override fun onItemClick(item: Asset) {
                    navigator.itemSelected(item)
                }
            })


    override fun loadData(isForceLoad: Boolean?) {
        navigator.showLoading(true)
        compositeDisposable.add(
            dataManager.getUserProfileDB().concatMap { dataManager.getAsserts(it.data?.profileId.toString()) }
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : CallbackWrapper<List<Asset>>() {
                    override fun onSuccess(dataResponse: List<Asset>) {
                        navigator.showLoading(false)
                        items.addAll(dataResponse)
                        navigator.showEmpty(dataResponse.isNullOrEmpty())
                    }

                    override fun onFailure(error: NetworkError) {
                        navigator.showLoading(false)
                        navigator.showEmpty(true)
                        handleError(error)
                    }
                })
        )
    }
}