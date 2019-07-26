package com.tebet.mojual.view.forgotpassword

import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.EmptyResponse
import com.tebet.mojual.data.models.UpdateProfileRequest
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class ForgotPasswordViewModel(dataManager: DataManager) :
    BaseViewModel<ForgotPasswordNavigator>(dataManager) {
    fun forgotPassword() {
        compositeDisposable.add(
            dataManager.updateProfile(
                UpdateProfileRequest()
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
}