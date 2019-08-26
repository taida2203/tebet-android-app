package com.tebet.mojual.view.home

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel

class HomeViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<HomeNavigator>(dataManager, schedulerProvider) {
    var profileLiveData = MutableLiveData<UserProfile>()

    var selectedTab = ObservableField(ScreenTab.Home)

    enum class ScreenTab {
        Home, Inbox, History
    }

    fun loadData() {
        compositeDisposable.add(
            dataManager.getUserProfileDB()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
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
        navigator.showOrderCompleteScreen(dataResponse)
    }

    fun onSubmitOrderNowClick(dataResponse: Order) {
        navigator.showAddContainerScreen(dataResponse)
    }

    fun onHomeClick() {
        selectedTab.set(ScreenTab.Home)
        navigator.showHomeScreen()
    }

    fun onHistoryClick() {
        selectedTab.set(ScreenTab.History)
        navigator.showHistoryScreen()
    }

    fun onQualityCheckClick() {
        navigator.showCheckQualityScreen()
    }

    fun onCheckQualityNowClick() {
        navigator.showSellNowScreen()
    }

    fun onQualityCheckOrderSelected(selectedItem: Order) {
        navigator.showAddContainerScreen(selectedItem)
    }

    fun onOrderDetailClick(item: Order) {
        navigator.showOrderDetailScreen(item)
    }
}