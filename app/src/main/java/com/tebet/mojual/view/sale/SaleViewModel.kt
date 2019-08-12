package com.tebet.mojual.view.sale

import android.widget.Toast
import androidx.databinding.ObservableField
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.Asset
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
    private var assets: List<Asset>? = null
    var selectedQuantity: ObservableField<Int> = ObservableField()
    var selectedFutureDate: ObservableField<Long> = ObservableField()

    fun onSubmitClick() {
        val currentCal = Calendar.getInstance()
        selectedFutureDate.set(currentCal.timeInMillis + 1000)

        if (selectedQuantity.get() == null || selectedFutureDate.get() == null) {
            navigator.show("Please fill all required field !!")
            return
        }
        navigator.showLoading(true)
        compositeDisposable.add(
            dataManager.createOrder(CreateOrderRequest(selectedQuantity.get(), selectedFutureDate.get()))
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

    fun loadData() {
        navigator.showLoading(true)
        compositeDisposable.add(
            dataManager.getUserProfileDB().concatMap { dataManager.getAsserts(it.data?.userId.toString()) }
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : CallbackWrapper<List<Asset>>() {
                    override fun onSuccess(dataResponse: List<Asset>) {
                        assets = dataResponse
                        navigator.showLoading(false)
                    }

                    override fun onFailure(error: String?) {
                        navigator.showLoading(false)
                        handleError(error)
                    }
                })
        )
    }

    fun openSelectQuantityScreen() {
        if (assets.isNullOrEmpty()) {
            navigator.show("Can't find list asset!!, try later")
            loadData()
            return
        }
        navigator.showQuantityScreen()
    }

    fun openSelectDateScreen() {
        navigator.showDateScreen()
    }
}