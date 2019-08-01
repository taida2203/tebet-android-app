package com.tebet.mojual.view.sale

import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.CreateOrderRequest
import com.tebet.mojual.data.models.EmptyResponse
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel

class SaleViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<SaleNavigator>(dataManager, schedulerProvider) {
    fun onSubmitClick() {
        navigator.showLoading(true)
        compositeDisposable.add(
            dataManager.createOrder(CreateOrderRequest(2, 1564647842696L))
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : CallbackWrapper<EmptyResponse>() {
                    override fun onSuccess(dataResponse: EmptyResponse) {
                        navigator.openSaleScreen()
                    }

                    override fun onFailure(error: String?) {
                        handleError(error)
                    }

                    override fun onComplete() {
                        super.onComplete()
                        navigator.showLoading(false)
                    }
                })
        )
    }
}