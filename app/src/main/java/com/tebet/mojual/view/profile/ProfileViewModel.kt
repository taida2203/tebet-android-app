package com.tebet.mojual.view.profile

import co.sdk.auth.AuthSdk
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.view.base.BaseViewModel
import io.reactivex.observers.DisposableObserver

class ProfileViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<ProfileNavigator>(dataManager, schedulerProvider) {

    fun logout() {
        navigator.showLoading(true)
        compositeDisposable.add(
            AuthSdk.instance.logout(true)
                .concatMap { dataManager.clearAllTables() }
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : DisposableObserver<Boolean>() {
                    override fun onComplete() {
                    }

                    override fun onNext(t: Boolean) {
                        navigator.showLoading(false)
                        navigator.openLoginScreen()
                    }

                    override fun onError(e: Throwable) {
                        navigator.showLoading(false)
                        navigator.openLoginScreen()
                    }
                })
        )
    }
}