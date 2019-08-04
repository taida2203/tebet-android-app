package com.tebet.mojual.view.signup.step1

import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.view.signup.step.SignUpInfoStepViewModel

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

    fun onSelectDateClick() {
        navigator.selectBirthDay()
    }
}