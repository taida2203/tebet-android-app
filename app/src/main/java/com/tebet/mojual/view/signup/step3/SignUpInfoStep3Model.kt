package com.tebet.mojual.view.signup.step3

import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.City
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseActivityNavigator
import com.tebet.mojual.view.base.BaseNavigator
import com.tebet.mojual.view.signup.step.SignUpInfoStepViewModel

class SignUpInfoStep3Model(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    SignUpInfoStepViewModel<BaseActivityNavigator>(dataManager, schedulerProvider) {
}