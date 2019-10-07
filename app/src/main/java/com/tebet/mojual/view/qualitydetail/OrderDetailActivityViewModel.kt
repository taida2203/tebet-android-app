package com.tebet.mojual.view.qualitydetail

import androidx.lifecycle.MutableLiveData
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.NetworkError
import com.tebet.mojual.data.models.OrderContainer
import com.tebet.mojual.data.models.OrderDetail
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import java.util.concurrent.TimeUnit

class OrderDetailActivityViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<OrderDetailActivityNavigator>(dataManager, schedulerProvider) {
    var userProfileLiveData: MutableLiveData<UserProfile> = MutableLiveData()

    override fun loadData(isForceLoad: Boolean?) {
        navigator.showLoading(true)
        compositeDisposable.add(
            dataManager.getUserProfileDB()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : CallbackWrapper<UserProfile>() {
                    override fun onSuccess(dataResponse: UserProfile) {
                        userProfileLiveData.value = dataResponse
                        navigator.showLoading(false)
                    }

                    override fun onFailure(error: NetworkError) {
                        navigator.showLoading(false)
                        handleError(error)
                    }
                })
        )
    }

    fun onSubmitClick() {
        navigator.showLoading(true)
        userProfileLiveData.value?.let { profile ->
            compositeDisposable.add(
                dataManager.updateProfile(profile)
                    .observeOn(schedulerProvider.ui())
                    .debounce(400, TimeUnit.MILLISECONDS)
                    .subscribeWith(object : CallbackWrapper<UserProfile>() {
                        override fun onSuccess(dataResponse: UserProfile) {
                            navigator.showLoading(false)
                            navigator.openPreviousScreen()
                        }

                        override fun onFailure(error: NetworkError) {
                            navigator.showLoading(false)
                            handleError(error)
                        }
                    })
            )
        }
    }

    fun onBankConfirmClick(order: OrderDetail, selectedItems: List<OrderContainer>) {
        navigator.onBankConfirmClick(order, selectedItems)
    }

    fun onReasonClick(order: OrderDetail, selectedItems: List<OrderContainer>) {
        navigator.onReasonClick(order, selectedItems)
    }
}