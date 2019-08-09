package com.tebet.mojual.view.loginpassword

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import co.sdk.auth.AuthSdk
import co.sdk.auth.core.AuthAccountKitMethod
import co.sdk.auth.core.AuthPasswordMethod
import co.sdk.auth.core.LoginConfiguration
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import me.tatarka.bindingcollectionadapter2.ItemBinding

class LoginWithPasswordViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<LoginWithPasswordNavigator>(dataManager, schedulerProvider) {
    var userInputPhone: String = ""
    var userInputPassword: String = ""
    var userInputPhonePrefix: String = ""

    var items: ObservableList<String> = ObservableArrayList()
    var itemBinding: ItemBinding<String> = ItemBinding.of(BR.item, R.layout.item_flag_dropdown)

    fun loadData() {
        items.add("84")
        items.add("85")
        items.add("86")
        userInputPhonePrefix = items[0]
    }

    fun doLogin() {
        if (!navigator.dataValid()) {
            return
        }
        navigator.showLoading(true)
        val configuration = LoginConfiguration(false)
        configuration.username = userInputPhonePrefix + userInputPhone.trim()
        configuration.password = userInputPassword.trim()
        navigator.activity()?.let {
            compositeDisposable.add(
                AuthSdk.instance.login(it, AuthPasswordMethod(), configuration)
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
                            when (dataResponse.statusEnum) {
                                UserProfile.Status.Init -> navigator.openRegistrationScreen()
                                UserProfile.Status.InitProfile -> navigator.openSignUpInfoScreen()
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