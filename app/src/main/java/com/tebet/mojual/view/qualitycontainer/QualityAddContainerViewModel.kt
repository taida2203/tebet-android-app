package com.tebet.mojual.view.qualitycontainer

import android.os.CountDownTimer
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.Asset
import com.tebet.mojual.data.models.ContainerWrapper
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.models.request.UpdatePasswordRequest
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.tatarka.bindingcollectionadapter2.OnItemBind
import java.util.concurrent.TimeUnit

class QualityAddContainerViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<QualityAddContainerNavigator>(dataManager, schedulerProvider) {
    val headerItem = ContainerWrapper(-1)
    var timer: CountDownTimer? = null
    var assignedContainers: ArrayList<Asset> = ArrayList()
    var order = MutableLiveData<Order>()
    var items: ObservableArrayList<ContainerWrapper> = ObservableArrayList()
    val onItemBind: OnItemBind<ContainerWrapper> = OnItemBind { itemBinding, position, item ->
        itemBinding.set(
            BR.item,
            if (position == 0) R.layout.item_quality_add_container_add else R.layout.item_quality_add_container
        )
        itemBinding.bindExtra(BR.listener, object : OnFutureDateClick {
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
                            }
                        }
                        timer?.start()
                        ContainerWrapper.CheckStatus.CheckStatusChecking
                    }
                    ContainerWrapper.CheckStatus.CheckStatusChecking -> {
                        items.remove(item)
                        ContainerWrapper.CheckStatus.CheckStatusCheck
                    }
                    else -> ContainerWrapper.CheckStatus.CheckStatusCheck
                }
            }

            override fun onItemClick(item: ContainerWrapper) {
                if (item != headerItem) {
                    if (item.checking == ContainerWrapper.CheckStatus.CheckStatusDone) {
                        item.expanded = !item.expanded
                    }
                    return
                }
                if (items.firstOrNull { it.checking == ContainerWrapper.CheckStatus.CheckStatusChecking } != null) {
                    navigator.show("Please wait checking complete or cancel current checking to add more !!")
                    return
                }
                val containersLeft =
                    assignedContainers.toSet() - items.filter { it.id >= 0 }.map { it.assignedContainers[it.selectedItem] }.toSet()
                if (containersLeft.isEmpty()) {
                    navigator.show("You don't have any available containers !!")
                    return
                }
                items.forEach {
                    it.checking = ContainerWrapper.CheckStatus.CheckStatusDone
                    it.expanded = false
                }
                val newItem = ContainerWrapper(items.size.toLong(), containersLeft.toList())
                newItem.selectedItem = 0
                val newItems = arrayListOf(headerItem, newItem)
                newItems.addAll(items.filter { it.id >= 0 }.toList())
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

    fun loadData() {
        navigator.showLoading(true)
        compositeDisposable.add(
            dataManager.getUserProfileDB().concatMap { dataManager.getAsserts(it.data?.profileId.toString()) }
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : CallbackWrapper<List<Asset>>() {
                    override fun onSuccess(dataResponse: List<Asset>) {
                        assignedContainers.addAll(dataResponse)
                        items.add(headerItem)
                        navigator.showLoading(false)
                    }

                    override fun onFailure(error: String?) {
                        navigator.showLoading(false)
                        handleError(error)
                    }
                })
        )
    }

    fun onForgotPasswordClick() {
        if (!navigator.dataValid()) {
            return
        }
        navigator.showLoading(true)
        compositeDisposable.add(
            dataManager.updatePassword(
                UpdatePasswordRequest("")
            ).concatMap { dataManager.getProfile() }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribeWith(object : CallbackWrapper<UserProfile>() {
                    override fun onSuccess(dataResponse: UserProfile) {
                        navigator.showLoading(false)
                        navigator.openHomeScreen()
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
        fun onStartSensorClick(item: ContainerWrapper)
    }
}