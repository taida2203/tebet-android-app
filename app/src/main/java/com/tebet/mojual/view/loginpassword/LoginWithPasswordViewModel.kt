package com.tebet.mojual.view.loginpassword

import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.view.base.BaseViewModel

class LoginWithPasswordViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<LoginWithPasswordNavigator>(dataManager, schedulerProvider) {
    fun doLogin() {
        navigator.doLogin()
    }

    fun doForgotPassword() {
        navigator.doForgotPassword()
    }
}