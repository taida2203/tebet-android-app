package com.tebet.mojual.view.qualitycontainer

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import co.sdk.auth.core.models.AuthJson
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.common.util.toJson
import com.tebet.mojual.common.util.toSensor
import com.tebet.mojual.common.util.toSensors
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.Asset
import com.tebet.mojual.data.models.ContainerWrapper
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.data.models.Quality
import com.tebet.mojual.data.models.request.CreateOrderRequest
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.observers.DisposableObserver
import me.tatarka.bindingcollectionadapter2.OnItemBind
import java.util.concurrent.TimeUnit


class QualityAddContainerViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<QualityAddContainerNavigator>(dataManager, schedulerProvider) {
    var order = ObservableField<Order>()
    val headerItem = ContainerWrapper(-1)
    var assignedContainers: ArrayList<Asset> = ArrayList()
    var items: ObservableArrayList<ContainerWrapper> = ObservableArrayList()
    val onItemBind: OnItemBind<ContainerWrapper> = OnItemBind { itemBinding, position, item ->
        itemBinding.set(
            BR.item,
            if (position == 0) R.layout.item_quality_add_container_add else R.layout.item_quality_add_container
        )
        itemBinding.bindExtra(BR.listener, object : OnFutureDateClick {
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
                if (item != headerItem) {
                    if (item.checking == ContainerWrapper.CheckStatus.CheckStatusDone) item.expanded =
                        !item.expanded
                    return
                }
                if (!allCheckingComplete()) return
                items.firstOrNull { it.checking == ContainerWrapper.CheckStatus.CheckStatusCheck && it != headerItem }
                    ?.let {
                        saveCheckedContainerDB(it)
                    }
                items.forEach {
                    it.checking = ContainerWrapper.CheckStatus.CheckStatusDone
                    it.expanded = false
                }

                val containersLeft = getAvailableContainer()
                if (containersLeft.isEmpty()) {
                    navigator.show("You don't have any available containers !!")
                    return
                }
                val newItem = ContainerWrapper(
                    items.size.toLong(),
                    customerData = Quality(
                        orderId = order.get()!!.orderId.toLong(),
                        orderCode = order.get()!!.orderCode
                    )
                )
                newItem.assignedContainers.clear()
                newItem.assignedContainers.addAll(containersLeft.toList())
                newItem.selectedItem = 0
                newItem.selectedWeight = 0
                val newItems = arrayListOf(headerItem, newItem)
                newItems.addAll(items.filter { it != headerItem }.toList())
                items.clear()
                items.addAll(newItems)
//                Collections.sort(items) { item1, item2 ->
//                    if (item1.id < 0) return@sort 1
//                    if (item2.id < 0) return@sort 1
//                    return@sort item2.id.compareTo(item1.id)
//                }
            }
        })
    }

    private fun getAvailableContainer(): List<Asset> {
        return assignedContainers.filterNot { exeptItem ->
            items.filter { it != headerItem && it.checking == ContainerWrapper.CheckStatus.CheckStatusDone }
                .map { it.customerData }.firstOrNull { it.containerCode == exeptItem.code } != null
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
        compositeDisposable.add(
            dataManager.scanSensorDataMock()
                .concatMap { sensorData ->
                    Observable.fromCallable {
                        val scannedData = sensorData.string().toSensor()
                        val savedData = item.customerData.data?.toSensors() ?: arrayListOf()
                        savedData.add(scannedData)
                        item.customerData.data = savedData.toJson()
                        true
                    }
                }.subscribe()
        )
    }

    private fun saveCheckedContainerDB(item: ContainerWrapper) {
        compositeDisposable.add(
            dataManager.insertContainerCheck(item.customerData)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui()).subscribe()
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
                        true
                    }.subscribeOn(schedulerProvider.ui())
                }
                .observeOn(schedulerProvider.ui()).subscribe()
        )
    }

    private fun allCheckingComplete(): Boolean {
        if (items.firstOrNull { it.checking == ContainerWrapper.CheckStatus.CheckStatusChecking } != null) {
            navigator.show("Please wait checking complete or cancel current checking to add more !!")
            return false
        }
        return true
    }

    fun loadData() {
        items.add(headerItem)
        navigator.showLoading(true)
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
                    }

                    override fun onNext(response: Pair<List<Quality>, List<Asset>>) {
                        assignedContainers.addAll(response.second)
                        order.get()?.let { order ->
                            val cachedItemByOrder =
                                response.first.filter { it.orderCode == order.orderCode }
                            val unAvailableCachedItem =
                                cachedItemByOrder.filter { cachedContainer ->
                                    assignedContainers
                                        .firstOrNull { cachedContainer.containerCode == it.code } == null
                                }

                            // remove item that contain un available container
                            unAvailableCachedItem.forEach(this@QualityAddContainerViewModel::removeContainerDB)

                            items.addAll((cachedItemByOrder - unAvailableCachedItem).map {
                                val savedContainer = ContainerWrapper(
                                    id = items.size.toLong(),
                                    customerData = it
                                )
                                savedContainer.checking =
                                    ContainerWrapper.CheckStatus.CheckStatusDone
                                savedContainer
                            })
                        }
                        navigator.showLoading(false)
                    }

                    override fun onError(e: Throwable) {
                        handleError(e.toString())
                        navigator.showLoading(false)
                    }

                })
        )
    }

    fun onSubmitClick() {
        if (!allCheckingComplete()) return
        navigator.showLoading(true)
        compositeDisposable.add(
            Observable.just(order.get()!!)
                .concatMap {
                    when {
                        it.orderId < 0 -> dataManager.createOrder(CreateOrderRequest(it.quantity, it.planDate))
                        else -> Observable.just(AuthJson(null, "", it))
                    }
                }
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : CallbackWrapper<Order>() {
                    override fun onSuccess(dataResponse: Order) {
                        navigator.showLoading(false)
                        navigator.openConfirmScreen(dataResponse)
                    }

                    override fun onFailure(error: String?) {
                        navigator.showLoading(false)
                        handleError(error)
                    }
                })
        )
    }

    interface OnFutureDateClick {
        fun onItemClick(item: ContainerWrapper)
        fun onItemRemoveClick(item: ContainerWrapper)
        fun onStartSensorClick(item: ContainerWrapper)
    }
}
