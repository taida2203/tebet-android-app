package com.tebet.mojual.view.profile

import co.sdk.auth.AuthSdk
import co.sdk.auth.core.models.ApiCallBack
import co.sdk.auth.core.models.LoginException
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.view.base.BaseViewModel

class ProfileViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<ProfileNavigator>(dataManager, schedulerProvider) {

    fun logout() {
        AuthSdk.instance.logout(true, object : ApiCallBack<Any>() {
            override fun onSuccess(responeCode: Int, response: Any?) {
                navigator.openLoginScreen()
            }

            override fun onFailed(exeption: LoginException) {
                navigator.openLoginScreen()
            }
        })
    }
}