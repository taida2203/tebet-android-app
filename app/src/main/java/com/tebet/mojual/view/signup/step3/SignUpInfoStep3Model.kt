package com.tebet.mojual.view.signup.step3

import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.view.base.BaseNavigator
import com.tebet.mojual.view.signup.step2.SignUpInfoStepViewModel

class SignUpInfoStep3Model(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    SignUpInfoStepViewModel<BaseNavigator>(dataManager, schedulerProvider) {

}