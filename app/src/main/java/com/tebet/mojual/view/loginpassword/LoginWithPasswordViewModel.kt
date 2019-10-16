package com.tebet.mojual.view.loginpassword

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import co.sdk.auth.AuthSdk
import co.sdk.auth.core.AuthAccountKitMethod
import co.sdk.auth.core.AuthPasswordMethod
import co.sdk.auth.core.models.LoginConfiguration
import androidx.databinding.library.baseAdapters.BR
import co.sdk.auth.core.AuthGooglePhoneLoginMethod
import com.tebet.mojual.R
import com.tebet.mojual.common.util.BindingUtils
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.NetworkError
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import com.tebet.mojual.view.profile.ProfileViewModel
import io.reactivex.observers.DisposableObserver
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

    var onOkEditText: BindingUtils.OnOkInSoftKeyboardListener = object : BindingUtils.OnOkInSoftKeyboardListener() {
        override fun onOkInSoftKeyboard() {
            doLogin()
        }
    }

    companion object {
        const val PHONE_PREFIX_VN = "+84"
        const val PHONE_PREFIX_ID = "+62"
    }

    override fun loadData(isForceLoad: Boolean?) {
        items.add(PHONE_PREFIX_ID)
        items.add(PHONE_PREFIX_VN)
        userInputPhonePrefix = items[0]
    }

    fun doLogin() {
        if (!navigator.dataValid()) {
            return
        }
        val fullPhoneNumber =
            userInputPhonePrefix + if (userInputPhone.startsWith("0")) userInputPhone.substring(
                1,
                userInputPhone.length
            ).trim() else userInputPhone.trim()

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
                AuthSdk.instance.login(it, AuthGooglePhoneLoginMethod(), configuration)
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

    fun doForgotPassword() {
        navigator.showLoading(true)
        navigator.activity()?.let {
            forceLogout {
                compositeDisposable.add(
                    AuthSdk.instance.login(
                        it,
                        AuthGooglePhoneLoginMethod(),
                        LoginConfiguration(true)
                    ).doOnError {
                        navigator.showLoading(false)
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

                            override fun onFailure(error: NetworkError) {
                                navigator.showLoading(false)
                                handleError(error)
                            }
                        })
                )
            }
        }
    }
}