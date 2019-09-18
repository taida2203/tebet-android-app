package com.tebet.mojual.view.bankconfirm

import androidx.lifecycle.MutableLiveData
import com.tebet.mojual.R
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.data.models.OrderContainer
import com.tebet.mojual.data.models.OrderDetail
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel


class BankConfirmViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<BankConfirmNavigator>(dataManager, schedulerProvider) {
    var order: OrderDetail? = null
    var selectedItems: List<OrderContainer>? = null
    var userProfileLiveData: MutableLiveData<UserProfile> = MutableLiveData()

    fun onUpdateClick() {
        navigator.openBankUpdateScreen()
    }

    fun onSubmitClick() {
        order?.let { od ->
            selectedItems?.let { items ->
                submitOrder(items)
                return
            }
        }
        navigator.show(R.string.general_error)
    }

    override fun loadData(forceLoad: Boolean?) {
        navigator.showLoading(true)
        var updateProfileStream = dataManager.getUserProfileDB()
        if (forceLoad == false) updateProfileStream = dataManager.getProfile()
        compositeDisposable.add(
            updateProfileStream.subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : CallbackWrapper<UserProfile>() {
                    override fun onSuccess(dataResponse: UserProfile) {
                        userProfileLiveData.value = dataResponse
                        navigator.showLoading(false)
                    }

                    override fun onFailure(error: String?) {
                        navigator.showLoading(false)
                        handleError(error)
                    }
                })
        )
    }

    private fun submitOrder(selectedItems: List<OrderContainer>) {
        order?.let { order ->
            navigator.showLoading(true)
            compositeDisposable.add(
                dataManager.confirmOrder(order.orderId, selectedItems)
                    .observeOn(schedulerProvider.ui())
                    .subscribeWith(object : CallbackWrapper<Order>() {
                        override fun onSuccess(dataResponse: Order) {
                            navigator.showLoading(false)
                            this@BankConfirmViewModel.order?.let {
                                navigator.openOrderDetailScreen(it)
                            }
                        }

                        override fun onFailure(error: String?) {
                            navigator.showLoading(false)
                            handleError(error)
                        }
                    })
            )
        }
    }
}