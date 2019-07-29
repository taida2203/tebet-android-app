package com.tebet.mojual.view.home.content

import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseNavigator
import com.tebet.mojual.view.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomeContentViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<BaseNavigator>(dataManager, schedulerProvider) {

    fun loadProfile() {
        compositeDisposable.add(
            dataManager.getProfile()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CallbackWrapper<UserProfile>() {
                    override fun onSuccess(dataResponse: UserProfile) {
                        if (dataResponse.status.equals("INIT")) {
                        } else {
                        }
                    }

                    override fun onFailure(error: String?) {
                    }
                })
        )
    }
}