package com.tebet.mojual.view.home

import androidx.lifecycle.MutableLiveData
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomeViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<HomeNavigator>(dataManager, schedulerProvider) {
    var profileLiveData = MutableLiveData<UserProfile>()

    fun loadData() {
        compositeDisposable.add(
            dataManager.getUserProfileDB()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CallbackWrapper<UserProfile>() {
                    override fun onFailure(error: String?) {
                    }

                    override fun onSuccess(dataResponse: UserProfile) {
                        profileLiveData.value = dataResponse
                    }
                })
        )
    }

    fun onSellClick() {
        navigator.showSellScreen()
    }

    fun onSubmitOrderClick(dataResponse: Order) {
        navigator.showOrderDetailScreen(dataResponse)
    }

    fun onHomeClick() {
        navigator.showHomeScreen()
    }

    fun onHistoryClick() {
        navigator.showHistoryScreen()
    }

    fun onQualityCheckClick() {
        navigator.showCheckQualityScreen()
    }
}