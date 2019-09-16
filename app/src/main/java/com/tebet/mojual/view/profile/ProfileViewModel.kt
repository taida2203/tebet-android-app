package com.tebet.mojual.view.profile

import androidx.databinding.ObservableField
import co.common.util.LanguageUtil
import co.common.util.PreferenceUtils
import co.common.util.PreferenceUtils.getString
import co.common.view.dialog.LanguageChoiceDialog
import co.common.view.dialog.SingleChoiceDialog
import co.sdk.auth.AuthSdk
import com.google.firebase.iid.FirebaseInstanceId
import com.tebet.mojual.R
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.models.request.DeviceRegisterRequest
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import io.reactivex.Observable
import io.reactivex.observers.DisposableObserver

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

    fun onBasicInfoClick() {
        userProfile.get()?.let {
            it.basicInfo = it.basicInfo
        }
    }

    fun onChangePassClick() {
        navigator.openChangePasswordScreen()
    }


    fun onBankAccountClick() {
        userProfile.get()?.let {
            it.bankAccount = it.bankAccount
        }
    }

    fun onDomicileClick() {
        userProfile.get()?.domicileAddress?.let {
            it.expanded = !it.expanded
        }
    }

    fun onPickupClick() {
        userProfile.get()?.pickupAddress?.let {
            it.expanded = !it.expanded
        }
    }

    fun onChangeLanguageClick(){
        navigator.openChangeLanguageDialog()
    }

    fun currentLangauge(){
    }

}