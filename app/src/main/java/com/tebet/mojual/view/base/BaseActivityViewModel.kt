package com.tebet.mojual.view.base

import androidx.lifecycle.MutableLiveData
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.view.login.LoginNavigator

class BaseActivityViewModel(dataManager: DataManager) : BaseViewModel<BaseActivityNavigator>(dataManager) {
    var profileError: MutableLiveData<String> = MutableLiveData()

    fun onBackPressed() {
        navigator.onBackPressed()
    }
}