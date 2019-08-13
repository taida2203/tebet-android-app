package com.tebet.mojual.view.sale

import androidx.databinding.Observable
import androidx.databinding.Observable.OnPropertyChangedCallback
import androidx.databinding.ObservableField
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
    var selectedQuantity: ObservableField<Int> = ObservableField()
    var selectedFutureDate: ObservableField<Price> = ObservableField()
    var simulationPrice: ObservableField<Double> = ObservableField()
    val callback = object : OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable, propertyId: Int) =
            simulationPrice.set(selectedFutureDate.get()?.price?.let {
                selectedQuantity.get()?.toDouble()?.let { it1 -> it.times(it1).times(Asset.quantity) }
            })
    }

    fun onSubmitClick() {
        if (!navigator.validate()) {
            return
        }
        navigator.showLoading(true)
        compositeDisposable.add(
            dataManager.createOrder(
                CreateOrderRequest(
                    selectedQuantity.get(),
                    selectedFutureDate.get()?.date
                )
            )
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : CallbackWrapper<Order>() {
                    override fun onSuccess(dataResponse: Order) {
                        dataResponse.price = selectedFutureDate.get()?.price
                        dataResponse.totalPrice = simulationPrice.get()
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
        selectedQuantity.addOnPropertyChangedCallback(callback)
        selectedFutureDate.addOnPropertyChangedCallback(callback)

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