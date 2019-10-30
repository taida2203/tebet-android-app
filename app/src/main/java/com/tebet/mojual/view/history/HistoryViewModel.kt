package com.tebet.mojual.view.history

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.library.baseAdapters.BR
import com.tebet.mojual.R
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.NetworkError
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.data.models.Paging
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.models.request.SearchOrderRequest
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import io.reactivex.Observable
import me.tatarka.bindingcollectionadapter2.ItemBinding

class HistoryViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<HistoryNavigator>(dataManager, schedulerProvider) {
    var items: ObservableArrayList<Order> = ObservableArrayList()
    var itemBinding: ItemBinding<Order> =
        ItemBinding.of<Order>(BR.item, R.layout.item_history)
            .bindExtra(BR.listener, object : OnFutureDateClick {
                override fun onItemClick(item: Order) {
                    navigator.itemSelected(item)
                }
            })

    var searchRequest: ObservableField<SearchOrderRequest> = ObservableField(SearchOrderRequest(hasNoContainer = false))

    var userProfile: UserProfile? = null

    override fun loadData(isForceLoad: Boolean?) {
        if (isForceLoad == true) {
            compositeDisposable.clear()
            items.clear()
        }
        loadData(0)
    }

    fun loadData(page: Int = 0) {
        val offset = page * 10
        if (items.size >= offset) {
            navigator.showLoading(true)
            compositeDisposable.add(getUserProfile()
                .map {
                    it.profileId?.let { profileId ->
                        searchRequest.get()?.profileId = profileId
                    }
                    searchRequest.get()?.offset = page * 10
                    searchRequest.get()?.limit = 10
                    searchRequest.get()
                }
                .concatMap { dataManager.searchOrders(it) }
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : CallbackWrapper<Paging<Order>>() {
                    override fun onSuccess(dataResponse: Paging<Order>) {
                        dataResponse.data.forEach { order ->
                            when {
                                items.contains(order) -> items[items.indexOf(order)] = order
                                else -> items.add(order)
                            }
                        }
                        navigator.showLoading(false)
                        navigator.showEmpty(page == 0 && dataResponse.data.isNullOrEmpty())
                    }

                    override fun onFailure(error: NetworkError) {
                        navigator.showLoading(false)
                        handleError(error)
                    }
                })
            )
        }
    }

    private fun getUserProfile(): Observable<UserProfile> {
        return when (userProfile) {
            null -> dataManager.getUserProfileDB().concatMap {
                this.userProfile = it.data
                Observable.just(this.userProfile)
            }
            else -> Observable.just(this.userProfile)
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

    fun selectOrderStatus() {
        navigator.openOrderStatusPicker()
    }
}