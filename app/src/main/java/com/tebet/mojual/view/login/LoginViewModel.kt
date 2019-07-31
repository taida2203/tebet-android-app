package com.tebet.mojual.view.login

import co.sdk.auth.AuthSdk
import co.sdk.auth.core.AuthAccountKitMethod
import co.sdk.auth.core.LoginConfiguration
import co.sdk.auth.core.models.ApiCallBack
import co.sdk.auth.core.models.LoginException
import co.sdk.auth.core.models.Token
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.EmptyResponse
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
        AuthSdk.instance.login(
            navigator.activity(),
            AuthAccountKitMethod(),
            LoginConfiguration(logoutWhileExpired = false),
            object : ApiCallBack<Token>() {
                override fun onSuccess(responeCode: Int, response: Token?) {
                    navigator.showLoading(false)
                    loadProfile(true)
                }

                override fun onFailed(exeption: LoginException) {
                    if (exeption.errorCode == 502) {
                        navigator.showLoading(false)
                        return
                    }
                    val config = LoginConfiguration(
                        logoutWhileExpired = false,
                        token = AuthSdk.instance.getBrandLoginToken()?.token,
                        phone = AuthSdk.instance.getBrandLoginToken()?.phone
                    )
                    compositeDisposable.add(
                        dataManager.register(config).subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            .subscribeWith(object : CallbackWrapper<EmptyResponse>() {
                                override fun onSuccess(dataResponse: EmptyResponse) {
                                    AuthSdk.instance.login(
                                        navigator.activity(),
                                        AuthAccountKitMethod(),
                                        LoginConfiguration(logoutWhileExpired = false),
                                        object : ApiCallBack<Token>() {
                                            override fun onSuccess(responeCode: Int, response: Token?) {
                                                navigator.showLoading(false)
                                                loadProfile(true)
                                            }

                                            override fun onFailed(exeption: LoginException) {
                                                if (exeption.errorCode == 502) {
                                                    navigator.showLoading(false)
                                                    return
                                                }
                                            }
                                        })
                                }

                                override fun onFailure(error: String?) {
                                    navigator.showLoading(false)
                                }
                            }
                            ))
                }
            })
    }

    fun loadProfile(registrationFLow: Boolean) {
        compositeDisposable.add(
            dataManager.getProfile()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : CallbackWrapper<UserProfile>() {
                    override fun onSuccess(dataResponse: UserProfile) {
                        if (dataResponse.status.equals("INIT")) {
                            if (!registrationFLow) {
                                navigator.openUpdatePasswordScreen()
                            } else {
                                navigator.openRegistrationScreen()
                            }
                        } else {
                            navigator.openHomeScreen()
                        }
                    }

                    override fun onFailure(error: String?) {
                    }
                })
        )
    }
}