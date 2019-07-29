package com.tebet.mojual.view.base

import androidx.lifecycle.MutableLiveData
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager

class BaseActivityViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) : BaseViewModel<BaseActivityNavigator>(dataManager, schedulerProvider) {
    var profileError: MutableLiveData<String> = MutableLiveData()

    fun onBackPressed() {
        navigator.onBackPressed()
    }
}