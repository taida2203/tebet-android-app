package com.tebet.mojual.view.base

import androidx.lifecycle.MutableLiveData
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.view.login.LoginNavigator

class BaseActivityViewModel(dataManager: DataManager) : BaseViewModel<LoginNavigator>(dataManager) {
    var profileError: MutableLiveData<String> = MutableLiveData()

    fun onLoginClick() {
        navigator.openLoginScreen()
    }

    fun onRegistrationClick() {
        navigator.doAccountKitLogin(true)
    }
}