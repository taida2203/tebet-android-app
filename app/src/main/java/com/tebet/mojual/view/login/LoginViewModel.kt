package com.tebet.mojual.view.login

import co.sdk.auth.AuthSdk
import co.sdk.auth.core.AuthAccountKitMethod
import co.sdk.auth.core.models.LoginConfiguration
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

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
        navigator.activity()?.let { activity ->
            compositeDisposable.add(
                AuthSdk.instance.logout(true)
                    .concatMap {
                        AuthSdk.instance.login(activity, AuthAccountKitMethod(), LoginConfiguration(logoutWhileExpired = false))
                            .doOnError {
                                navigator.showLoading(false)
                                registerNewUser()
                            }
                    }
                    .concatMap { dataManager.getProfile() }
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
                Observable.just(true).delay(50, TimeUnit.MILLISECONDS)
                    .concatMap {
                        dataManager.register(
                            LoginConfiguration(
                                logoutWhileExpired = false,
                                token = AuthSdk.instance.getBrandLoginToken()?.token,
                                phone = AuthSdk.instance.getBrandLoginToken()?.phone
                            )
                        )
                    }.concatMap { _ ->
                        AuthSdk.instance.login(
                            it,
                            AuthAccountKitMethod(),
                            LoginConfiguration(logoutWhileExpired = false)
                        )
                    }.concatMap { dataManager.getProfile() }
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