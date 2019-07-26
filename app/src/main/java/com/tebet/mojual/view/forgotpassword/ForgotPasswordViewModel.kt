package com.tebet.mojual.view.forgotpassword

import androidx.lifecycle.MutableLiveData
import co.sdk.auth.AuthSdk
import co.sdk.auth.core.LoginConfiguration
import com.tebet.mojual.data.models.UpdateProfileRequest
import com.tebet.mojual.data.repository.ProfileRepository
import com.tebet.mojual.view.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ForgotPasswordViewModel @Inject constructor(private var profileRepository: ProfileRepository) :
    BaseViewModel<ForgotPasswordNavigator>() {

    var profileError: MutableLiveData<String> = MutableLiveData()

    fun forgotPassword() {
        compositeDisposable.add(
            profileRepository.forgotPasswordApi(
                UpdateProfileRequest()
            ).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribe({
                }, {
                })
        )
    }
}