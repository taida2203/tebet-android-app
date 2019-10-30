package com.tebet.mojual.view.help

import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.view.base.BaseViewModel

class QualityHelpViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<QualityHelpNavigator>(dataManager, schedulerProvider) {
    fun onForgotPasswordClick() {
        navigator.openHomeScreen()
    }
}