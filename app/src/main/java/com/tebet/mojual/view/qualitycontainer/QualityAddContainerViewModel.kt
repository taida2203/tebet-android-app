package com.tebet.mojual.view.qualitycontainer

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
    var assignedContainers: ArrayList<Asset> = ArrayList()
    var order = MutableLiveData<Order>()
    var items: ObservableArrayList<ContainerWrapper> = ObservableArrayList()
    val onItemBind: OnItemBind<ContainerWrapper> = OnItemBind { itemBinding, position, item ->
        itemBinding.set(BR.item, if (position == 0) R.layout.item_quality_add_container_add else R.layout.item_quality_add_container)
        itemBinding.bindExtra(BR.listener, object : OnFutureDateClick {
            override fun onItemClick(item: ContainerWrapper) {
                items.filter { it.id >=0 }.map { it.assignedContainers[it.selectedItem] }.toTypedArray().distinctBy {  }
                if (assignedContainers.size <= 0) {
                    navigator.show("You have 0 container left !!")
                }
                items.forEach { it.checking = false }
                val newItem = ContainerWrapper(items.size.toLong(), assignedContainers)
                newItem.checking = true
                items.add(newItem)
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
                        items.add(ContainerWrapper(-1))
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
    }
}