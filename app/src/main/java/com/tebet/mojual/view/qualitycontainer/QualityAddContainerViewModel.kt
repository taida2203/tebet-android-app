package com.tebet.mojual.view.qualitycontainer

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.library.baseAdapters.BR
import co.sdk.auth.core.models.AuthJson
import com.tebet.mojual.R
import com.tebet.mojual.common.adapter.OnListItemClick
import com.tebet.mojual.common.util.*
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.*
import com.tebet.mojual.data.models.request.CreateOrderRequest
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.observers.DisposableObserver
import me.tatarka.bindingcollectionadapter2.collections.MergeObservableList
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class QualityAddContainerViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<QualityAddContainerNavigator>(dataManager, schedulerProvider) {
    constructor(
        dataManager: DataManager,
        schedulerProvider: SchedulerProvider,
        sensorManager: Sensor
    ) : this(dataManager, schedulerProvider) {
        this.sensorManager.set(sensorManager)
    }

    var order = ObservableField<Order>()
    var sensorManager = ObservableField<Sensor>()
    var assignedContainers: ArrayList<Asset> = ArrayList()
    var items: ObservableArrayList<ContainerWrapper> = ObservableArrayList()


    /**
     * Items merged with a header on top
     */
    var headerFooterItems: MergeObservableList<Any> = MergeObservableList<Any>()
        .insertItem(generateAddButtonContent())
        .insertList(items)

    private fun generateAddButtonContent(): String? {
        return String.format(
            Utility.getInstance().getString(R.string.check_quality_add_container_add_new_container),
            items.size + 1,
            order.get()?.quantity ?: 0
        )
    }

    val onItemBind: OnItemBindClass<Any> = OnItemBindClass<Any>()
        .map(String::class.java) { itemBinding, _, _ ->
            itemBinding.set(BR.item, R.layout.item_quality_add)
                .bindExtra(BR.listener, object : OnListItemClick<String> {
                    override fun onItemClick(item: String) {
                        if (!allCheckingComplete()) return
                        val containersLeft = getAvailableContainer()
                        if (containersLeft.isEmpty()) {
                            navigator.show(R.string.check_quality_add_container_no_available_container)
                            return
                        }
                        val newItem = ContainerWrapper(
                            customerData = Quality(
                                orderId = order.get()!!.orderId,
                                orderCode = order.get()!!.orderCode
                            )
                        )
                        newItem.assignedContainers.clear()
                        newItem.assignedContainers.addAll(containersLeft.toList())
                        newItem.selectedItem = 0
                        newItem.selectedWeight = 0
//                        newItem.sensorConnected = sensorManager.isConnected
                        val newItems = arrayListOf(newItem) + items
                        items.clear()
                        items.addAll(newItems)
                        refreshCheckButtonState()
                    }
                })
        }
        .map(ContainerWrapper::class.java) { itemBinding, _, _ ->
            itemBinding.set(BR.item, R.layout.item_quality_add_container)
                .bindExtra(BR.listener, object : OnFutureDateClick {
                    override fun onItemRemoveClick(item: ContainerWrapper) {
                        removeContainerDB(item.customerData)
                    }

                    override fun onStartSensorClick(item: ContainerWrapper) {
                        compositeDisposable.clear()
                        item.timeCountDown = null
                        item.checking = when (item.checking) {
                            ContainerWrapper.CheckStatus.CheckStatusCheck -> {
                                countDownChecking(item)
                                ContainerWrapper.CheckStatus.CheckStatusChecking
                            }
                            ContainerWrapper.CheckStatus.CheckStatusChecking -> {
                                removeContainerDB(item.customerData)
                                ContainerWrapper.CheckStatus.CheckStatusCheck
                            }
                            else -> ContainerWrapper.CheckStatus.CheckStatusCheck
                        }
                    }

                    override fun onItemClick(item: ContainerWrapper) {
                        if (item.checking == ContainerWrapper.CheckStatus.CheckStatusDone) item.expanded =
                            !item.expanded
                    }
                })
        }

    val isReachMaxAtPlan: Boolean
        get() {
            order.get()?.quantity?.let { return items.size >= it }
            return false
        }

    private fun saveUnFinishItem() {
        items.filter { it.checking == ContainerWrapper.CheckStatus.CheckStatusCheck }.forEach {
            it.checking = ContainerWrapper.CheckStatus.CheckStatusDone
            it.expanded = false
            saveCheckedContainerDB(it)
        }
    }

    private fun getAvailableContainer(): List<Asset> {
        return assignedContainers.filterNot { exeptItem ->
            items.filter { it.checking == ContainerWrapper.CheckStatus.CheckStatusDone }
                .map { it.customerData }.firstOrNull { it.assetCode == exeptItem.code } != null
        }
    }

    private fun countDownChecking(item: ContainerWrapper) {
        compositeDisposable.add(Observable.interval(1, TimeUnit.SECONDS)
            .take(item.time + 1)
            .map { v -> item.time - v }
            .doOnNext { if (it.rem(5) == 0L) collectSensorData(item) }
            .subscribeWith(object : DisposableObserver<Long>() {
                override fun onComplete() {
                    item.timeCountDown = null
                    item.checking = ContainerWrapper.CheckStatus.CheckStatusDone
                    saveCheckedContainerDB(item)
                }

                override fun onNext(t: Long) {
                    item.timeCountDown = t - 1
                }

                override fun onError(e: Throwable) {
                }
            })
        )
    }

    private fun collectSensorData(item: ContainerWrapper) {
        if (sensorManager.get()?.isConnected == false) return
        compositeDisposable.add(
            dataManager.scanSensorData()
                .concatMap { sensorData ->
                    Observable.fromCallable {
                        val scannedData = sensorData.string().toSensor()
                        val savedData = item.customerData.sensorData?.toSensors() ?: arrayListOf()
                        savedData.add(scannedData)
                        item.customerData.sensorData = savedData.toJson()
                        true
                    }
                }.subscribe({}, {error-> Timber.e(error)})
        )
    }

    private fun saveCheckedContainerDB(item: ContainerWrapper) {
        compositeDisposable.add(
            dataManager.insertContainerCheck(item.customerData)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui()).subscribe({}, {error-> Timber.e(error)})
        )
    }

    private fun removeContainerDB(item: Quality) {
        compositeDisposable.add(
            dataManager.deleteContainerCheck(item)
                .subscribeOn(schedulerProvider.io())
                .concatMap { _ ->
                    Observable.fromCallable {
                        items.remove(items.firstOrNull { it.customerData == item })
                        items.firstOrNull { it.checking == ContainerWrapper.CheckStatus.CheckStatusCheck }
                            ?.let {
                                it.assignedContainers.clear()
                                it.assignedContainers.addAll(getAvailableContainer())
                                it.selectedItem = 0
                            }
                        refreshCheckButtonState()
                        true
                    }.subscribeOn(schedulerProvider.ui())
                }
                .observeOn(schedulerProvider.ui()).subscribe({}, {error-> Timber.e(error)})
        )
    }

    private fun refreshCheckButtonState(forceRefresh: Boolean = false) {
        if (!isReachMaxAtPlan || forceRefresh) {
            headerFooterItems.removeAll()
            headerFooterItems = MergeObservableList<Any>()
                .insertItem(generateAddButtonContent())
                .insertList(items)
        } else if (isReachMaxAtPlan && (headerFooterItems[0] is String || forceRefresh)) {
            headerFooterItems.removeAll()
            headerFooterItems = MergeObservableList<Any>()
                .insertList(items)
        }
    }

    private fun allCheckingComplete(): Boolean {
        if (items.firstOrNull { it.checking == ContainerWrapper.CheckStatus.CheckStatusChecking || it.checking == ContainerWrapper.CheckStatus.CheckStatusCheck } != null) {
            navigator.show(R.string.check_quality_add_container_block_while_checking)
            return false
        }
        order.get()?.quantity?.let {
            if (items.size > it) {
                navigator.show(String.format(Utility.getInstance().getString(R.string.check_quality_add_container_max_order_not_allowed), it))
                return false
            }
        }
        return true
    }

    override fun loadData(isForceLoad: Boolean?) {
        sensorManager.get()?.connectIOTCount = 0
        sensorManager.addOnPropertyChangedCallback(object : androidx.databinding.Observable.OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: androidx.databinding.Observable?, propertyId: Int) {
                if (propertyId == BR.connected) {
//                    items.firstOrNull { it.checking == ContainerWrapper.CheckStatus.CheckStatusCheck }?.let {
//                        it.sensorConnected = sensorManager.isConnected
//                    }
                }
            }
        })
        refreshCheckButtonState(true)
        navigator.showLoading(true)
        sensorManager.get()?.status = SensorStatus.CONNECTING
        compositeDisposable.add(
            Observable.zip(
                dataManager.getContainerCheckDB(),
                dataManager.getUserProfileDB().concatMap { dataManager.getAsserts(it.data?.profileId.toString()) },
                BiFunction<List<Quality>, AuthJson<List<Asset>>, Pair<List<Quality>, List<Asset>>>
                { qualities, assets -> Pair(qualities, assets.data ?: emptyList()) }
            )
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : DisposableObserver<Pair<List<Quality>, List<Asset>>>() {
                    override fun onComplete() {
                        retryConnectIOT()
                    }

                    override fun onNext(response: Pair<List<Quality>, List<Asset>>) {
                        assignedContainers.addAll(response.second)
                        order.get()?.let { order ->
                            val cachedItemByOrder =
                                response.first.filter { it.orderCode == order.orderCode }
                            val unAvailableCachedItem =
                                cachedItemByOrder.filter { cachedContainer ->
                                    assignedContainers.firstOrNull { cachedContainer.assetCode == it.code } == null
                                }

                            // remove item that contain un available container
                            unAvailableCachedItem.forEach(this@QualityAddContainerViewModel::removeContainerDB)

                            items.addAll((cachedItemByOrder - unAvailableCachedItem).map {
                                val savedContainer = ContainerWrapper(customerData = it)
                                savedContainer.checking = ContainerWrapper.CheckStatus.CheckStatusDone
                                savedContainer
                            })
                            refreshCheckButtonState()
                        }
                        navigator.showLoading(false)
                    }

                    override fun onError(e: Throwable) {
                        handleError(NetworkError(e))
                        navigator.showLoading(false)
                    }

                })
        )
    }

    fun onTipsClick() {
        navigator.openQualityHelpScreen()
    }

    fun onSubmitClick() {
        if (!allCheckingComplete()) return
        sensorManager.get()?.let { sensor ->
            if (sensor.isEnabled) navigator.showTurnOffIOTDialog()
            navigator.showLoading(true)
            compositeDisposable.add(
                navigator.releaseNetworkRoute()
                    .concatMap { sensor.connectNetworkWifi() }
                    .concatMap {
                        Observable.just(order.get()!!)
                            .concatMap {
                                when {
                                    it.orderId < 0 -> dataManager.createOrder(
                                        CreateOrderRequest(
                                            it.quantity,
                                            it.planDate,
                                            items.map { item -> item.customerData })
                                    )
                                    else -> dataManager.updateOrderQuality(
                                        it.orderId,
                                        items.map { item -> item.customerData })
                                }
                            }
                            .concatMap { apiResponse ->
                                Observable.fromCallable {
                                    items.map { wrapper -> wrapper.customerData }
                                        .forEach(this@QualityAddContainerViewModel::removeContainerDB)
                                    true
                                }.concatMap { Observable.just(apiResponse) }
                            }
                    }.subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribeWith(object : CallbackWrapper<Order>() {
                        override fun onSuccess(dataResponse: Order) {
                            navigator.showLoading(false)
                            navigator.openConfirmScreen(dataResponse)
                        }

                        override fun onFailure(error: NetworkError) {
                            navigator.showLoading(false)
                            handleError(error)
                        }
                    })
            )
        }
    }

    fun retryConnectIOT() {
        if (sensorManager.get()?.connectIOTCount ?: 0 >= Sensor.retryMax) {
            navigator.connectIOTManual()
        } else {
            navigator.requestLocationAndConnectIOT()
        }
    }
    fun connectIOT() {
        sensorManager.get()?.let {
            compositeDisposable.add(
                it.connectIOTWifi()
                    .concatMap { navigator.routeNetworkRequestsThroughWifi(Sensor.sensorSSID) }
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe({}, {})
            )
        }
    }

    fun refreshIOTStatus() {
        sensorManager.get()?.let {
            compositeDisposable.add(
                it.refreshStatus()
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui()).subscribe({}, {})
            )
        }
    }

    interface OnFutureDateClick {
        fun onItemClick(item: ContainerWrapper)
        fun onItemRemoveClick(item: ContainerWrapper)
        fun onStartSensorClick(item: ContainerWrapper)
    }
}
