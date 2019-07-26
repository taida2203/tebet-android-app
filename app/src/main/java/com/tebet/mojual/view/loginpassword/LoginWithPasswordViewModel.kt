package com.tebet.mojual.view.loginpassword

import androidx.lifecycle.MutableLiveData
import com.tebet.mojual.view.base.BaseViewModel
import javax.inject.Inject

class LoginWithPasswordViewModel @Inject constructor() :
    BaseViewModel<LoginWithPasswordNavigator>() {
    var profileError: MutableLiveData<String> = MutableLiveData()

    fun doLogin() {
     navigator.doLogin()
    }
    fun doForgotPassword() {
     navigator.doForgotPassword()
    }
}