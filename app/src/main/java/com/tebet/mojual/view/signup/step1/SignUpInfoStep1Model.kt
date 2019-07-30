package com.tebet.mojual.view.signup.step1

import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.signup.step2.SignUpInfoStepViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.concurrent.TimeUnit

class SignUpInfoStep1Model(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    SignUpInfoStepViewModel<SignUpInfoStep1Navigator>(dataManager, schedulerProvider) {
    fun onCaptureAvatar() {
        navigator.captureAvatar()
    }

    fun onCaptureEKTP() {
        navigator.captureEKTP()
    }
}