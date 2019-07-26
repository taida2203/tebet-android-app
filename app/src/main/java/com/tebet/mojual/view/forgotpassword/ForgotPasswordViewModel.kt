package com.tebet.mojual.view.forgotpassword

import androidx.lifecycle.MutableLiveData
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.UpdateProfileRequest
import com.tebet.mojual.view.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class ForgotPasswordViewModel(dataManager: DataManager) :
    BaseViewModel<ForgotPasswordNavigator>(dataManager) {

    var profileError: MutableLiveData<String> = MutableLiveData()

    fun forgotPassword() {
        compositeDisposable.add(
            dataManager.updateProfile(
                UpdateProfileRequest()
            ).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribe({
                    navigator.openHomeScreen()
                }, {
                })
        )
    }
}