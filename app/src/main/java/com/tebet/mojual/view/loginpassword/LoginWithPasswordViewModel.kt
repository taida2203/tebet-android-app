package com.tebet.mojual.view.loginpassword

import com.tebet.mojual.data.DataManager
import com.tebet.mojual.view.base.BaseViewModel

class LoginWithPasswordViewModel(dataManager: DataManager) :
    BaseViewModel<LoginWithPasswordNavigator>(dataManager) {
    fun doLogin() {
        navigator.doLogin()
    }

    fun doForgotPassword() {
        navigator.doForgotPassword()
    }
}