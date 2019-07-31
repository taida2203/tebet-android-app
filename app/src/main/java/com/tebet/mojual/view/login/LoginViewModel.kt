package com.tebet.mojual.view.login

import co.sdk.auth.AuthSdk
import co.sdk.auth.core.AuthAccountKitMethod
import co.sdk.auth.core.LoginConfiguration
import co.sdk.auth.core.models.ApiCallBack
import co.sdk.auth.core.models.AuthJson
import co.sdk.auth.core.models.LoginException
import co.sdk.auth.core.models.Token
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.EmptyResponse
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import io.reactivex.Observable

class LoginViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<LoginNavigator>(dataManager, schedulerProvider) {

    fun onLoginClick() {
        navigator.openLoginScreen()
    }

    fun onRegistrationClick() {
        navigator.showLoading(true)
        compositeDisposable.add(
            AuthSdk.instance.login(
                navigator.activity(),
                AuthAccountKitMethod(),
                LoginConfiguration(logoutWhileExpired = false)
            ).doOnError { error ->
                registerNewUser()
            }
                .concatMap { result ->
                    dataManager.getProfile()
                        .observeOn(schedulerProvider.ui())
                }
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : CallbackWrapper<UserProfile>() {
                    override fun onSuccess(dataResponse: UserProfile) {
                        when {
                            dataResponse.status.equals("INIT") -> navigator.openRegistrationScreen()
                            else -> navigator.openHomeScreen()
                        }
                    }

                    override fun onFailure(error: String?) {
                        handleError(error)
                    }

                    override fun onComplete() {
                        super.onComplete()
                        navigator.showLoading(false)
                    }
                })
        )
    }

    private fun registerNewUser() {
        compositeDisposable.add(
            dataManager.register(
                LoginConfiguration(
                    logoutWhileExpired = false,
                    token = AuthSdk.instance.getBrandLoginToken()?.token,
                    phone = AuthSdk.instance.getBrandLoginToken()?.phone
                )
            )
                .concatMap { registerResponse ->
                    AuthSdk.instance.login(
                        navigator.activity(),
                        AuthAccountKitMethod(),
                        LoginConfiguration(logoutWhileExpired = false)
                    )
                }.concatMap { loginResponse -> dataManager.getProfile() }
                .subscribeWith(object :
                    CallbackWrapper<UserProfile>() {
                    override fun onSuccess(dataResponse: UserProfile) {
                        when {
                            dataResponse.status.equals("INIT") -> navigator.openRegistrationScreen()
                            else -> navigator.openHomeScreen()
                        }
                    }

                    override fun onFailure(error: String?) {
                        handleError(error)
                    }

                    override fun onComplete() {
                        super.onComplete()
                        navigator.showLoading(false)
                    }
                })
        )
    }
}