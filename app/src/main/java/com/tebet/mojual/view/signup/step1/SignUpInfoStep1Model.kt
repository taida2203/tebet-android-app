package com.tebet.mojual.view.signup.step1

import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SignUpInfoStep1Model(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<SignUpInfoStep1Navigator>(dataManager, schedulerProvider) {

    fun onCaptureAvatar() {
        navigator.captureAvatar()
    }

    fun onCaptureEKTP() {
        navigator.captureEKTP()
    }
}