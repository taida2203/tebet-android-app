package com.tebet.mojual.view.loginpassword

import co.sdk.auth.AuthSdk
import co.sdk.auth.core.AuthAccountKitMethod
import co.sdk.auth.core.AuthPasswordMethod
import co.sdk.auth.core.LoginConfiguration
import co.sdk.auth.core.models.ApiCallBack
import co.sdk.auth.core.models.LoginException
import co.sdk.auth.core.models.Token
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel

class LoginWithPasswordViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<LoginWithPasswordNavigator>(dataManager, schedulerProvider) {
    var userInputPhone: String = ""
    var userInputPassword: String = ""

    fun doLogin() {
        if (!navigator.dataValid()) {
            return
        }
        navigator.showLoading(true)
        val configuration = LoginConfiguration(false)
        configuration.username = userInputPhone
        configuration.password = userInputPassword
        navigator.activity()?.let {
            compositeDisposable.add(
                AuthSdk.instance.login(it, AuthPasswordMethod(), configuration)
                    .concatMap { result -> dataManager.getProfile() }
                    .observeOn(schedulerProvider.ui())
                    .subscribeWith(object : CallbackWrapper<UserProfile>() {
                        override fun onSuccess(dataResponse: UserProfile) {
                            navigator.showLoading(false)
                            when {
                                dataResponse.status.equals("INIT") -> navigator.openRegistrationScreen()
                                dataResponse.status.equals("INIT_PROFILE") -> navigator.openSignUpInfoScreen()
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

    fun doForgotPassword() {
        navigator.showLoading(true)
        navigator.activity()?.let {
            compositeDisposable.add(
                AuthSdk.instance.logout(true).concatMap { _ ->
                    AuthSdk.instance.login(it, AuthAccountKitMethod(), LoginConfiguration(true)).doOnError {
                        navigator.showLoading(false)
                    }
                }
                    .concatMap { dataManager.getProfile() }
                    .observeOn(schedulerProvider.ui())
                    .subscribeWith(object : CallbackWrapper<UserProfile>() {
                        override fun onSuccess(dataResponse: UserProfile) {
                            navigator.showLoading(false)
                            when {
                                dataResponse.status.equals("INIT") -> navigator.openRegistrationScreen()
                                dataResponse.status.equals("INIT_PROFILE") -> navigator.openSignUpInfoScreen()
                                else -> navigator.openForgotPasswordScreen()
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