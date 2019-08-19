package com.tebet.mojual.view.qualitycontainer

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.common.util.BindingUtils
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.data.models.Paging
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.models.request.SearchOrderRequest
import com.tebet.mojual.data.models.request.UpdatePasswordRequest
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import com.tebet.mojual.view.qualitycheck.QualityViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.tatarka.bindingcollectionadapter2.OnItemBind
import java.util.concurrent.TimeUnit

class QualityAddContainerViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<QualityAddContainerNavigator>(dataManager, schedulerProvider) {
    var order = MutableLiveData<Order>()
    var items: ObservableArrayList<Order> = ObservableArrayList()
    val onItemBind: OnItemBind<Order> = OnItemBind { itemBinding, position, item ->
        itemBinding.set(BR.item, if (position == 0) R.layout.item_quality_add_container_add else R.layout.item_quality_add_container)
        itemBinding.bindExtra(BR.listener, object : QualityViewModel.OnFutureDateClick {
            override fun onItemClick(item: Order) {
                if (item.orderId >= 0) {
                    item.isSelected = true
                    items.filterNot { adapterItem -> adapterItem.orderId == item.orderId }.forEach { it.isSelected = false }
                }
            }
        })
    }

    var userInputPassword: String = ""
    var onOkEditText: BindingUtils.OnOkInSoftKeyboardListener = object : BindingUtils.OnOkInSoftKeyboardListener() {
        override fun onOkInSoftKeyboard() {
            onForgotPasswordClick()
        }
    }

    fun loadData(page: Int = 0) {
        val offset = page * 10
        if (items.size >= offset) {
            navigator.showLoading(true)
            compositeDisposable.add(
                dataManager.searchOrders(SearchOrderRequest(offset = offset, limit = 10))
                    .observeOn(schedulerProvider.ui())
                    .subscribeWith(object : CallbackWrapper<Paging<Order>>() {
                        override fun onSuccess(dataResponse: Paging<Order>) {
                            items.add(Order(-1, ""))
//                            items.addAll(dataResponse.data)
                            navigator.showLoading(false)
                        }

                        override fun onFailure(error: String?) {
                            navigator.showLoading(false)
                            handleError(error)
                            items.clear()
                            items.add(Order(-1, ""))
                        }
                    })
            )
        }
    }

    fun onForgotPasswordClick() {
        if (!navigator.dataValid()) {
            return
        }
        navigator.showLoading(true)
        compositeDisposable.add(
            dataManager.updatePassword(
                UpdatePasswordRequest(userInputPassword.trim())
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
}