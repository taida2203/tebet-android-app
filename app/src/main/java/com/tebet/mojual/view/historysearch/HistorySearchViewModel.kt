package com.tebet.mojual.view.historysearch

import androidx.databinding.ObservableField
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.NetworkError
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.models.request.SearchOrderRequest
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel


class HistorySearchViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<HistorySearchNavigator>(dataManager, schedulerProvider) {
    var searchRequest: ObservableField<SearchOrderRequest> = ObservableField()

    override fun loadData(isForceLoad: Boolean?) {
        compositeDisposable.add(
            dataManager.getUserProfileDB()
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : CallbackWrapper<UserProfile>() {
                    override fun onSuccess(dataResponse: UserProfile) {
                        navigator.showLoading(false)
                        dataResponse.profileId?.let {
                            searchRequest.get()?.profileId = it
                        }
                    }

                    override fun onFailure(error: NetworkError) {
                        navigator.showLoading(false)
                        handleError(error)
                    }
                })
        )
    }

    fun selectFromDate() {
        navigator.openFromDatePicker()
    }

    fun selectToDate() {
        navigator.openToDatePicker()
    }

    fun selectOrderBy() {
        navigator.openOrderByPicker()
    }

    fun selectOrderType() {
        navigator.openOrderTypePicker()
    }

    fun selectOrderStatus() {
        navigator.openOrderStatusPicker()
    }

    fun submit() {
        searchRequest.get()?.let {
            it.toPlanDate?.let { toPlanDate ->
                it.toPlanDate = toPlanDate + (24 * 60 * 60 * 1000 - 1) // a day
            }
            navigator.openHistoryScreen(it)
        }
    }
}