package com.tebet.mojual.view.sale

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.Asset
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.data.models.Price
import com.tebet.mojual.data.models.request.CreateOrderRequest
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel


class SaleViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<SaleNavigator>(dataManager, schedulerProvider) {
    private var assets: List<Asset>? = null
    var selectedQuantity: MutableLiveData<Int> = MutableLiveData()
    var selectedFutureDate: MutableLiveData<Price> = MutableLiveData()
    var simulationPrice: MutableLiveData<Double> = MediatorLiveData<Double>().apply {
        val calculatePrice = CalculatePrice()
        addSource(selectedQuantity, calculatePrice)
        addSource(selectedFutureDate, calculatePrice)
    }

    private inner class CalculatePrice : Observer<Any> {
        override fun onChanged(ignored: Any?) {
            simulationPrice.value = selectedFutureDate.value?.price?.let {
                selectedQuantity.value?.toDouble()?.let { it1 -> it.times(it1).times(Asset.quantity) }
            }
        }
    }

    fun onSubmitClick() {
        if (!navigator.validate()) {
            return
        }
        navigator.showLoading(true)
        compositeDisposable.add(
            dataManager.createOrder(
                CreateOrderRequest(
                    selectedQuantity.value,
                    selectedFutureDate.value?.date
                )
            )
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : CallbackWrapper<Order>() {
                    override fun onSuccess(dataResponse: Order) {
                        dataResponse.price = selectedFutureDate.value?.price
                        dataResponse.totalPrice = simulationPrice.value
                        navigator.showLoading(false)
                        navigator.openSaleScreen(dataResponse)
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