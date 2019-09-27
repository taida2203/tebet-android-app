package com.tebet.mojual.view.profilepin

import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.view.base.BaseViewModel

class PinCodeViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<PinCodeNavigator>(dataManager, schedulerProvider) {
    fun onGoBackHomeScreen() {
        navigator.openHomeScreen()
    }
}