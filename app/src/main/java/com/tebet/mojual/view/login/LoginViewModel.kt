package com.tebet.mojual.view.login

import androidx.lifecycle.MutableLiveData
import co.sdk.auth.AuthSdk
import co.sdk.auth.core.LoginConfiguration
import co.sdk.auth.core.models.AuthJson
import com.google.android.gms.auth.api.Auth
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.EmptyResponse
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class LoginViewModel(dataManager: DataManager) :
    BaseViewModel<LoginNavigator>(dataManager) {
    var profileError: MutableLiveData<String> = MutableLiveData()

    fun onLoginClick() {
        navigator.openLoginScreen()
    }

    fun onRegistrationClick() {
        navigator.doAccountKitLogin(true)
    }

    fun loadProfile() {
//        compositeDisposable.add(
//            dataManager.getProfile()
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .debounce(400, TimeUnit.MILLISECONDS)
//                .subscribe({ profileResponse ->
//                    if (profileResponse.data?.status.equals("INIT")) {
//                        navigator.openRegistrationScreen()
//                    } else {
//                        navigator.openHomeScreen()
//                    }
//                }, { e ->
//                    navigator.openLoginScreen()
//                })
//        )

        compositeDisposable.add(
            dataManager.getProfile()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribeWith(object : CallbackWrapper<UserProfile>() {
                    override fun onSuccess(dataResponse: UserProfile) {
                    }

                    override fun onFailure(error: String?) {
                    }
                })
        )
    }

    fun register() {
        compositeDisposable.add(
            dataManager.register(
                LoginConfiguration(
                    logoutWhileExpired = false,
                    token = AuthSdk.instance.getBrandLoginToken()?.token,
                    phone = AuthSdk.instance.getBrandLoginToken()?.phone
                )
            ).subscribeWith(object : CallbackWrapper<EmptyResponse>() {
                override fun onSuccess(dataResponse: EmptyResponse) {
                    navigator.doAccountKitLogin(false)
                }
                override fun onFailure(error: String?) {
                }
            }
            ))
    }

}