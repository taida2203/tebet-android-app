package com.tebet.mojual.view.home.content

import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.view.base.BaseNavigator
import com.tebet.mojual.view.base.BaseViewModel

class HomeContentViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<BaseNavigator>(dataManager, schedulerProvider) {
}