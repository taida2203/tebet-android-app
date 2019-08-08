package com.tebet.mojual.view.selectquantity

import androidx.lifecycle.MutableLiveData
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SelectQuantityViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<SelectQuantityNavigator>(dataManager, schedulerProvider) {
    var profileLiveData = MutableLiveData<UserProfile>()

    fun loadData() {
        compositeDisposable.add(
            dataManager.getUserProfileDB()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CallbackWrapper<UserProfile>() {
                    override fun onFailure(error: String?) {
                    }

                    override fun onSuccess(dataResponse: UserProfile) {
                        profileLiveData.value = dataResponse
                    }
                })
        )
    }

}