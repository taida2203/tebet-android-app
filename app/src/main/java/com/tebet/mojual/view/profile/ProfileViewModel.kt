package com.tebet.mojual.view.home.content

import co.sdk.auth.AuthSdk
import co.sdk.auth.core.models.ApiCallBack
import co.sdk.auth.core.models.LoginException
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseNavigator
import com.tebet.mojual.view.base.BaseViewModel
import com.tebet.mojual.view.profile.ProfileNavigator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProfileViewModel(dataManager: DataManager) :
    BaseViewModel<ProfileNavigator>(dataManager) {

    fun loadProfile() {
        compositeDisposable.add(
            dataManager.getProfile()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CallbackWrapper<UserProfile>() {
                    override fun onSuccess(dataResponse: UserProfile) {
                        if (dataResponse.status.equals("INIT")) {
                        } else {
                        }
                    }

                    override fun onFailure(error: String?) {
                    }
                })
        )
    }

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