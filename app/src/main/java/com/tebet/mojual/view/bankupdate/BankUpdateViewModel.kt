package com.tebet.mojual.view.bankupdate

import androidx.lifecycle.MutableLiveData
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import java.util.concurrent.TimeUnit

class BankUpdateViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<BankUpdateNavigator>(dataManager, schedulerProvider) {
    var userProfileLiveData: MutableLiveData<UserProfile> = MutableLiveData()

    override fun loadData() {
        navigator.showLoading(true)
        compositeDisposable.add(
            dataManager.getUserProfileDB()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : CallbackWrapper<UserProfile>() {
                    override fun onSuccess(dataResponse: UserProfile) {
                        userProfileLiveData.value = dataResponse
                        navigator.showLoading(false)
                    }

                    override fun onFailure(error: String?) {
                        navigator.showLoading(false)
                        handleError(error)
                    }
                })
        )
    }

    fun onSubmitClick() {
        navigator.showLoading(true)
        userProfileLiveData.value?.let { profile ->
            compositeDisposable.add(
                dataManager.updateProfile(profile)
                    .observeOn(schedulerProvider.ui())
                    .debounce(400, TimeUnit.MILLISECONDS)
                    .subscribeWith(object : CallbackWrapper<UserProfile>() {
                        override fun onSuccess(dataResponse: UserProfile) {
                            navigator.showLoading(false)
                            navigator.openPreviousScreen()
                        }

                        override fun onFailure(error: String?) {
                            navigator.showLoading(false)
                            handleError(error)
                        }
                    })
            )
        }
    }
}