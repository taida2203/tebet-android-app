package com.tebet.mojual.view.historysearch

import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.view.base.BaseViewModel


class HistorySearchViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<HistorySearchNavigator>(dataManager, schedulerProvider) {
    fun loadData(page: Int = 0) {
    }
}