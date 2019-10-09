package com.tebet.mojual.view.home

import androidx.databinding.ObservableField
import androidx.databinding.ObservableLong
import androidx.lifecycle.MutableLiveData
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.*
import com.tebet.mojual.data.models.request.SearchOrderRequest
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel

class HomeViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<HomeNavigator>(dataManager, schedulerProvider) {
    var profileLiveData = MutableLiveData<UserProfile>()
    var unreadCount = ObservableLong()

    var selectedTab = ObservableField(ScreenTab.Home)

    enum class ScreenTab {
        Home, Message, History
    }

    override fun loadData(isForceLoad: Boolean?) {
        compositeDisposable.add(
            dataManager.getUserProfileDB()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : CallbackWrapper<UserProfile>() {
                    override fun onFailure(error: NetworkError) {
                        handleError(error)
                    }

                    override fun onSuccess(dataResponse: UserProfile) {
                        profileLiveData.value = dataResponse
                    }
                })
        )
        updateUnReadCount()
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
        this.onHistoryClick(SearchOrderRequest())
    }

    fun onHistoryClick(searchOrderRequest: SearchOrderRequest?) {
        selectedTab.set(ScreenTab.History)
        navigator.showHistoryScreen(searchOrderRequest)
    }

    fun onMessageClick() {
        selectedTab.set(ScreenTab.Message)
        navigator.showInboxScreen()
    }

    fun onQualityCheckClick() {
        navigator.showCheckQualityScreen()
    }

    fun onCheckQualityNowClick() {
        navigator.showSellScreen()
    }

    fun onQualityCheckOrderSelected(selectedItem: Order) {
        navigator.showAddContainerScreen(selectedItem)
    }

    fun onOrderDetailClick(item: Order) {
        navigator.showOrderDetailScreen(item)
    }

    fun onReasonClick(order: OrderDetail, selectedItems: List<OrderContainer>) {
        navigator.showRejectReasonScreen(order, selectedItems)
    }

    fun onSearchAdvancedClick(searchOrderRequest: SearchOrderRequest) {
        navigator.showHistorySearchScreen(searchOrderRequest)
    }

    fun onBankConfirmClick(order: OrderDetail, selectedItems: List<OrderContainer>) {
        navigator.showBankConfirmScreen(order, selectedItems)
    }

    fun showUnreadCount(unreadCountResponse: Long) {
        unreadCount.set(unreadCountResponse)
    }

    fun updateUnReadCount() {
        compositeDisposable.add(dataManager.getUnreadCount().subscribeWith(object :
            CallbackWrapper<Long>() {
            override fun onSuccess(dataResponse: Long) {
                showUnreadCount(dataResponse)
            }

            override fun onFailure(error: NetworkError) {
            }
        }))
    }
}