package com.tebet.mojual.view.profilepin

import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel

class PinCodeViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<PinCodeNavigator>(dataManager, schedulerProvider) {
    fun onGoBackHomeScreen() {
        navigator.openHomeScreen()
    }

    override fun loadData(isForceLoad: Boolean?) {
        super.loadData(isForceLoad)
        compositeDisposable.add(
            dataManager.getProfile()
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : CallbackWrapper<UserProfile>() {
                    override fun onSuccess(dataResponse: UserProfile) {
                    }

                    override fun onFailure(error: String?) {
                    }

                    override fun onComplete() {
                    }
                })
        )
    }
}