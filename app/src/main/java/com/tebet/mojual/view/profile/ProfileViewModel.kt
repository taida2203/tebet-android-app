package com.tebet.mojual.view.profile

import androidx.databinding.ObservableField
import co.common.constant.AppConstant
import co.common.util.LanguageUtil
import co.common.util.PreferenceUtils
import co.common.util.PreferenceUtils.getString
import co.common.view.dialog.LanguageChoiceDialog
import co.common.view.dialog.SingleChoiceDialog
import co.sdk.auth.AuthSdk
import com.google.firebase.iid.FirebaseInstanceId
import com.tebet.mojual.R
import com.tebet.mojual.common.util.Utility
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.models.request.DeviceRegisterRequest
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import io.reactivex.Observable
import io.reactivex.observers.DisposableObserver
import java.util.concurrent.TimeUnit

class ProfileViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<ProfileNavigator>(dataManager, schedulerProvider) {
    var pin : ObservableField<String> = ObservableField("")
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

    override fun loadData(isForceLoad: Boolean?) {
        pin.set(getString(AppConstant.PIN_CODE, ""))
        compositeDisposable.add(
            dataManager.getUserProfileDB()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : CallbackWrapper<UserProfile>() {
                    override fun onFailure(error: String?) {
                        handleError(error)
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

    fun onPinCodeClick() {
        navigator.openPinCodeScreen()
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

    fun doChangeLanguage(selectedItem: String?) {
        when (selectedItem) {
            Utility.getInstance().getString(R.string.support_language_english) -> {
                if (LanguageUtil.instance.getLanguageIndex() == LanguageUtil.LANGUAGE_INDEX_ENGLISH) return
                userProfile.get()?.language = UserProfile.LANGUAGE_EN
            }
            Utility.getInstance().getString(R.string.support_language_bahasa) -> {
                if (LanguageUtil.instance.getLanguageIndex() == LanguageUtil.LANGUAGE_INDEX_BAHASA) return
                userProfile.get()?.language = UserProfile.LANGUAGE_IN
            }
        }
        userProfile.get()?.let {
            navigator.showLoading(true)
            compositeDisposable.add(
                dataManager.updateProfile(it)
                    .concatMap { dataManager.getProfile() }
                    .observeOn(schedulerProvider.ui())
                    .debounce(400, TimeUnit.MILLISECONDS)
                    .subscribeWith(object : CallbackWrapper<UserProfile>() {
                        override fun onSuccess(dataResponse: UserProfile) {
                            navigator.showLoading(false)
                            userProfile.set(dataResponse)
                            navigator.changeLanguage(selectedItem)
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