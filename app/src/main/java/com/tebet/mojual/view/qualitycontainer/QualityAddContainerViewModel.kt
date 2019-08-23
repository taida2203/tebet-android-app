package com.tebet.mojual.view.qualitycontainer

import android.os.CountDownTimer
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import co.sdk.auth.core.models.AuthJson
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.*
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import me.tatarka.bindingcollectionadapter2.OnItemBind

class QualityAddContainerViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<QualityAddContainerNavigator>(dataManager, schedulerProvider) {
    var order = ObservableField<Order>()
    val headerItem = ContainerWrapper(-1)
    var timer: CountDownTimer? = null
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
                timer?.cancel()
                item.timeCountDown = null
                item.checking = when (item.checking) {
                    ContainerWrapper.CheckStatus.CheckStatusCheck -> {
                        timer = object : CountDownTimer(item.time * 1000, 1000) {
                            override fun onTick(millisUntilFinished: Long) {
                                item.timeCountDown = millisUntilFinished / 1000
                            }

                            override fun onFinish() {
                                item.timeCountDown = null
                                item.checking = ContainerWrapper.CheckStatus.CheckStatusDone
                                saveCheckedContainerDB(item)
                            }
                        }
                        timer?.start()
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
                val containersLeft =
                    assignedContainers.filterNot { exeptItem ->
                        items.filter { it != headerItem }.map { it.customerData }.firstOrNull { it.containerCode == exeptItem.code } != null
                    }
                if (containersLeft.isEmpty()) {
                    navigator.show("You don't have any available containers !!")
                    return
                }
                items.firstOrNull { it.checking == ContainerWrapper.CheckStatus.CheckStatusCheck && it != headerItem }
                    ?.let {
                        saveCheckedContainerDB(it)
                    }
                items.forEach {

                    it.checking = ContainerWrapper.CheckStatus.CheckStatusDone
                    it.expanded = false
                }
                val newItem = ContainerWrapper(
                    items.size.toLong(),
                    containersLeft.toList(),
                    customerData = Quality(
                        orderId = order.get()?.orderId?.toLong() ?: -1,
                        orderCode = order.get()?.orderCode ?: ""
                    )
                )
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

    private fun saveCheckedContainerDB(item: ContainerWrapper) {
        compositeDisposable.add(
            dataManager.insertContainerCheck(item.customerData)
                .subscribeOn(Schedulers.newThread())
                .observeOn(schedulerProvider.ui()).subscribe()
        )
    }

    private fun removeContainerDB(item: Quality) {
        compositeDisposable.add(
            dataManager.deleteContainerCheck(item)
                .subscribeOn(Schedulers.newThread())
                .observeOn(schedulerProvider.ui()).subscribeWith(object :
                    DisposableObserver<Boolean>() {
                    override fun onComplete() {
                    }

                    override fun onNext(t: Boolean) {
                        items.remove(items.firstOrNull{it.customerData == item})
                    }

                    override fun onError(e: Throwable) {
                        items.remove(items.firstOrNull{it.customerData == item})
                    }
                })
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
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<Pair<List<Quality>, List<Asset>>>() {
                    override fun onComplete() {
                    }

                    override fun onNext(response: Pair<List<Quality>, List<Asset>>) {
                        assignedContainers.addAll(response.second)
                        order.get()?.let { order ->
                            val cachedItemByOrder =response.first.filter { it.orderCode == order.orderCode }
                            val unAvailableCachedItem = cachedItemByOrder.filter { cachedContainer -> assignedContainers
                                .firstOrNull { cachedContainer.containerCode == it.code } == null }

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
            dataManager.getContainerCheckDB()
                .concatMap { dataManager.getAsserts("33") }
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : CallbackWrapper<List<Asset>>() {
                    override fun onSuccess(dataResponse: List<Asset>) {
                        navigator.showLoading(false)
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