package com.tebet.mojual.view.login

import co.sdk.auth.AuthSdk
import co.sdk.auth.core.AuthGooglePhoneLoginMethod
import co.sdk.auth.core.models.LoginConfiguration
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.NetworkError
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import com.tebet.mojual.view.profile.ProfileViewModel
import io.reactivex.Observable
import io.reactivex.observers.DisposableObserver
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
            forceLogout {
                compositeDisposable.add(
                    AuthSdk.instance.login(
                        activity,
                        AuthGooglePhoneLoginMethod(),
                        LoginConfiguration(logoutWhileExpired = false)
                    )
                        .doOnError {
                            navigator.showLoading(false)
                            registerNewUser()
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

                            override fun onFailure(error: NetworkError) {
                                navigator.showLoading(false)
                                handleError(error)
                            }
                        })
                )
            }
        }
    }

    private fun forceLogout(function: () -> Unit) {
        compositeDisposable.add(
            ProfileViewModel.logoutStream(dataManager)
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : DisposableObserver<Any>() {
                    override fun onComplete() {
                    }

                    override fun onNext(t: Any) {
                        function()
                    }

                    override fun onError(e: Throwable) {
                        function()
                    }
                })
        )
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
                            AuthGooglePhoneLoginMethod(),
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

                        override fun onFailure(error: NetworkError) {
                            navigator.showLoading(false)
                            handleError(error)
                        }
                    })
            )
        }
    }
}