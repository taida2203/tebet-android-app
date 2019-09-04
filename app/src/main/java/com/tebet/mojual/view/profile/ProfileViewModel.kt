package com.tebet.mojual.view.profile

import androidx.databinding.ObservableField
import co.sdk.auth.AuthSdk
import com.google.firebase.iid.FirebaseInstanceId
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.models.request.DeviceRegisterRequest
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import io.reactivex.Observable
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
            Observable.create<String> { emitter ->
                FirebaseInstanceId.getInstance().instanceId
                    .addOnSuccessListener { emitter.onNext(it.token) }
                    .addOnFailureListener { emitter.onError(it) }
                    .addOnCanceledListener { emitter.onError(Throwable()) }
            }
                .concatMap { dataManager.unRegisterDevice(DeviceRegisterRequest(it)) }
                .concatMap { AuthSdk.instance.logout(true) }
                .doOnComplete { dataManager.clearAllTables().subscribe({}, {}) }
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : DisposableObserver<Any>() {
                    override fun onComplete() {
                    }

                    override fun onNext(t: Any) {
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
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
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