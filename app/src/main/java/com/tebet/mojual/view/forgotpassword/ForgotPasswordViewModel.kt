package com.tebet.mojual.view.forgotpassword

import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.common.view.AppEditText
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.EmptyResponse
import com.tebet.mojual.data.models.UpdatePasswordRequest
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class ForgotPasswordViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<ForgotPasswordNavigator>(dataManager, schedulerProvider) {
    var userInputPassword: String = ""

    fun onForgotPasswordClick() {
        if (!navigator.dataValid()) {
            return
        }
        navigator.showLoading(true)
        compositeDisposable.add(
            dataManager.updatePassword(
                UpdatePasswordRequest(userInputPassword.trim())
            ).concatMap { dataManager.getProfile() }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribeWith(object : CallbackWrapper<UserProfile>() {
                    override fun onSuccess(dataResponse: UserProfile) {
                        navigator.showLoading(false)
                        navigator.openHomeScreen()
                    }

                    override fun onFailure(error: String?) {
                        navigator.showLoading(false)
                        handleError(error)
                    }
                })
        )
    }
}