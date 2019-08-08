package com.tebet.mojual.view.login

import co.sdk.auth.AuthSdk
import co.sdk.auth.core.AuthAccountKitMethod
import co.sdk.auth.core.LoginConfiguration
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel

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
        navigator.activity()?.let {
            compositeDisposable.add(
                AuthSdk.instance.logout(true).concatMap { _ ->
                    AuthSdk.instance.login(
                        it,
                        AuthAccountKitMethod(),
                        LoginConfiguration(logoutWhileExpired = false)
                    ).doOnError { error -> registerNewUser() }
                }
                    .concatMap { result -> dataManager.getProfile() }
                    .observeOn(schedulerProvider.ui())
                    .subscribeWith(object : CallbackWrapper<UserProfile>() {
                        override fun onSuccess(dataResponse: UserProfile) {
                            navigator.showLoading(false)
                            when (dataResponse.statusEnum) {
                                UserProfile.Status.Init -> navigator.openRegistrationScreen()
                                UserProfile.Status.InitProfile -> navigator.openSignUpInfoScreen()
                                else -> navigator.openHomeScreen()
                            }
                        }

                        override fun onFailure(error: String?) {
                            navigator.showLoading(false)
                        }
                    })
            )
        }
    }

    private fun registerNewUser() {
        navigator.activity()?.let {
            navigator.showLoading(true)
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
                            it,
                            AuthAccountKitMethod(),
                            LoginConfiguration(logoutWhileExpired = false)
                        )
                    }.concatMap { loginResponse -> dataManager.getProfile() }
                    .subscribeWith(object :
                        CallbackWrapper<UserProfile>() {
                        override fun onSuccess(dataResponse: UserProfile) {
                            navigator.showLoading(false)
                            when (dataResponse.statusEnum) {
                                UserProfile.Status.Init -> navigator.openRegistrationScreen()
                                UserProfile.Status.InitProfile -> navigator.openSignUpInfoScreen()
                                else -> navigator.openHomeScreen()
                            }
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