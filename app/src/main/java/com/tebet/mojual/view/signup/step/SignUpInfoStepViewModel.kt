package com.tebet.mojual.view.signup.step

import androidx.databinding.ObservableField
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.view.base.BaseViewModel

open class SignUpInfoStepViewModel<N>(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<N>(dataManager, schedulerProvider) {
    open var userProfile: ObservableField<UserProfile> = ObservableField()
}