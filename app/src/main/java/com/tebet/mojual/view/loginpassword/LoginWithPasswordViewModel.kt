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
    var userInputPhonePrefix: String = PHONE_PREFIX_ID

    var items: ObservableList<String> = ObservableArrayList()
    var itemBinding: ItemBinding<String> = ItemBinding.of(BR.item, R.layout.item_flag_dropdown)

    companion object {
        const val PHONE_PREFIX_VN = "+84"
        const val PHONE_PREFIX_ID = "+62"
    }

    fun loadData() {
        items.add(PHONE_PREFIX_VN)
        items.add(PHONE_PREFIX_ID)
        userInputPhonePrefix = items[0]
    }

    fun doLogin() {
        if (!navigator.dataValid()) {
            return
        }
        val fullPhoneNumber = userInputPhonePrefix + userInputPhone.trim()
        if (!when (userInputPhonePrefix) {
                PHONE_PREFIX_ID -> {
                    fullPhoneNumber.matches(Regex("(\\+62((\\d{3}([ -]\\d{3,})([- ]\\d{4,})?)|(\\d+)))|(\\(\\d+\\) \\d+)|\\d{3}( \\d+)+|(\\d+[ -]\\d+)|\\d+"))
                }
                PHONE_PREFIX_VN -> {
                    fullPhoneNumber.matches(Regex("(\\+84|0)\\d{9,10}"))
                }
                else -> true
            }
        ) {
            navigator.show("Phone number +$fullPhoneNumber is wrong format")
            return
        }
        navigator.showLoading(true)
        val configuration = LoginConfiguration(false)
        configuration.username = fullPhoneNumber.replace("+", "")
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