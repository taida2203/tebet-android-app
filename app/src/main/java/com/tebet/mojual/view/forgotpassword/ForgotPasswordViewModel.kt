package com.tebet.mojual.view.forgotpassword

import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.common.view.AppEditText
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.EmptyResponse
import com.tebet.mojual.data.models.UpdateProfileRequest
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
    fun forgotPassword(tvPassword: AppEditText?) {
        compositeDisposable.add(
            dataManager.updateProfile(
                UpdateProfileRequest(tvPassword?.text?.trim().toString())
            ).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribeWith(object : CallbackWrapper<EmptyResponse>() {
                    override fun onSuccess(dataResponse: EmptyResponse) {
                        navigator.openHomeScreen()
                    }

                    override fun onFailure(error: String?) {
                    }
                })
        )
    }

    fun onForgotPasswordClick() {
        navigator.forgotPassword()
    }
}