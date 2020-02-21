package com.tebet.mojual.view.sale

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableInt
import androidx.databinding.ObservableList
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import co.sdk.auth.core.models.AuthJson
import com.tebet.mojual.R
import com.tebet.mojual.common.util.Sensor
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.Asset
import com.tebet.mojual.data.models.NetworkError
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.data.models.Price
import com.tebet.mojual.data.models.enumeration.ContainerOrderType
import com.tebet.mojual.data.models.request.CreateOrderRequest
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import io.reactivex.Observable
import me.tatarka.bindingcollectionadapter2.ItemBinding


class SaleViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<SaleNavigator>(dataManager, schedulerProvider) {
    constructor(
        dataManager: DataManager,
        schedulerProvider: SchedulerProvider,
        sensorManager: Sensor
    ) : this(dataManager, schedulerProvider) {
        this.sensorManager = sensorManager
    }

    private lateinit var sensorManager: Sensor
    private var assets: List<Asset>? = null
    var selectedQuantity: MutableLiveData<Int> = MutableLiveData()
    var selectedFutureDate: MutableLiveData<Price> = MutableLiveData()
    var simulationPrice: MutableLiveData<Double> = MediatorLiveData<Double>().apply {
        val calculatePrice = CalculatePrice()
        addSource(selectedQuantity, calculatePrice)
        addSource(selectedFutureDate, calculatePrice)
    }

    var itemTypeBinding: ItemBinding<Int> = ItemBinding.of(BR.item, R.layout.item_container_type_dropdown)
    var itemTypes: ObservableList<ContainerOrderType> = ObservableArrayList()
    var selectedItemType: ObservableInt = ObservableInt(0)

    private inner class CalculatePrice : Observer<Any> {
        override fun onChanged(ignored: Any?) {
            simulationPrice.value = selectedFutureDate.value?.pricePerContainer?.let {
                selectedQuantity.value?.toDouble()?.let { it1 -> it.times(it1) }
            }
        }
    }

    fun onSubmitClick() {
        if (!navigator.validate()) {
            return
        }

        navigator.showLoading(true)
        compositeDisposable.add(
            Observable.just(Object()).concatMap {
                when {
                    selectedFutureDate.value?.isToday == true -> Observable.just(
                        AuthJson(
                            null,
                            "",
                            Order(
                                -2,
                                "",
                                quantity = selectedQuantity.value,
                                planDate = selectedFutureDate.value?.date,
                                containerType = getSelectedContainerType()?.name
                            )
                        )
                    )
                    else -> dataManager.createOrder(
                        CreateOrderRequest(
                            quantity = selectedQuantity.value,
                            planDate = selectedFutureDate.value?.date,
                            containerType = getSelectedContainerType()?.name
                        )
                    )
                }
            }
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : CallbackWrapper<Order>() {
                    override fun onSuccess(dataResponse: Order) {
                        dataResponse.price = selectedFutureDate.value?.pricePerKg
                        dataResponse.totalPrice = simulationPrice.value
                        navigator.showLoading(false)
                        navigator.openSaleDetailScreen(dataResponse)
                    }

                    override fun onFailure(error: NetworkError) {
                        navigator.showLoading(false)
                        handleError(error)
                    }
                })
        )
    }

    fun getSelectedContainerType(): ContainerOrderType? {
        val selectedContainerType = try {
            itemTypes[selectedItemType.get()]
        } catch (e: Exception) {
            ContainerOrderType.OTHER
        }
        if (selectedContainerType != ContainerOrderType.OTHER) {
            return selectedContainerType
        }
        return null
    }

    override fun loadData(isForceLoad: Boolean?) {
        navigator.showLoading(true)
        compositeDisposable.add(
            sensorManager.connectNetworkWifi()
                .concatMap { dataManager.getUserProfileDB() }
                .concatMap { dataManager.getAsserts(it.data?.profileId.toString()) }
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : CallbackWrapper<List<Asset>>() {
                    override fun onSuccess(dataResponse: List<Asset>) {
                        assets = dataResponse
                        navigator.showLoading(false)
                    }

                    override fun onFailure(error: NetworkError) {
                        navigator.showLoading(false)
                        handleError(error)
                    }
                })
        )
        if (itemTypes.size <= 0) {
            itemTypes.addAll(
                arrayListOf(
                    ContainerOrderType.OTHER,
                    ContainerOrderType.JERRYCAN,
                    ContainerOrderType.DRUM
                )
            )
        }
    }

    fun openSelectQuantityScreen() {
        if (assets.isNullOrEmpty()) {
            navigator.showEmptyAsset()
            loadData()
            return
        }
        navigator.showQuantityScreen()
    }

    fun openSelectContainerScreen() {
        if (assets.isNullOrEmpty()) {
            navigator.showEmptyAsset()
            loadData()
            return
        }
        navigator.showQuantityScreen()
    }

    fun openSelectDateScreen() {
        navigator.showDateScreen()
    }
}