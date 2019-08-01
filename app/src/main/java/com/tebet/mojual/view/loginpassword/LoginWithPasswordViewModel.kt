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

                    }
                    )
            )
        }
    }

    fun doForgotPassword() {
        navigator.showLoading(true)
        navigator.activity()?.let {
            AuthSdk.instance.login(
                it, AuthAccountKitMethod(), LoginConfiguration(true)
                , object : ApiCallBack<Token>() {
                    override fun onSuccess(responeCode: Int, response: Token?) {
                        navigator.showLoading(false)
                        navigator.openForgotPasswordScreen()
                    }

                    override fun onFailed(exeption: LoginException) {
                        if (exeption.errorCode == 502) return
                        AuthSdk.instance.logout(true, object : ApiCallBack<Any>() {
                            override fun onSuccess(responeCode: Int, response: Any?) {
                                navigator.showLoading(false)
                                doForgotPassword()
                            }

                            override fun onFailed(exeption: LoginException) {
                                navigator.showLoading(false)
                            }

                        })
                        handleError(exeption.errorMessage)
                    }
                })
        }
    }
}