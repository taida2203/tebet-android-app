package com.tebet.mojual.view.profile

import androidx.databinding.ObservableField
import co.sdk.auth.AuthSdk
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class ProfileViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<ProfileNavigator>(dataManager, schedulerProvider) {
    var userProfile: ObservableField<UserProfile> = ObservableField()
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
    fun loadData() {
        compositeDisposable.add(
            dataManager.getUserProfileDB()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CallbackWrapper<UserProfile>() {
                    override fun onFailure(error: String?) {
                    }

                    override fun onSuccess(dataResponse: UserProfile) {
                        userProfile.set(dataResponse)
                    }
                })
        )
    }
}