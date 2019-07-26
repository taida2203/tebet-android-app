package com.tebet.mojual.view.base

import androidx.lifecycle.MutableLiveData
import com.tebet.mojual.view.login.LoginNavigator
import javax.inject.Inject

class BaseActivityViewModel @Inject constructor() :
    BaseViewModel<LoginNavigator>() {
    var profileError: MutableLiveData<String> = MutableLiveData()

    fun onLoginClick() {
        navigator.openLoginScreen()
    }

    fun onRegistrationClick() {
        navigator.doAccountKitLogin(true)
    }
}