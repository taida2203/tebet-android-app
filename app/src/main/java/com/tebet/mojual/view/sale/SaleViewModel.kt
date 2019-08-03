package com.tebet.mojual.view.sale

import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.CreateOrderRequest
import com.tebet.mojual.data.models.EmptyResponse
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import java.util.*

class SaleViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<SaleNavigator>(dataManager, schedulerProvider) {
    fun onSubmitClick() {
        var currentCal = Calendar.getInstance()
        navigator.showLoading(true)
        compositeDisposable.add(
            dataManager.createOrder(CreateOrderRequest(2, currentCal.timeInMillis + 1000))
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : CallbackWrapper<EmptyResponse>() {
                    override fun onSuccess(dataResponse: EmptyResponse) {
                        navigator.showLoading(false)
                        navigator.openSaleScreen()
                    }

                    override fun onFailure(error: String?) {
                        navigator.showLoading(false)
                        handleError(error)
                    }
                })
        )
    }
}