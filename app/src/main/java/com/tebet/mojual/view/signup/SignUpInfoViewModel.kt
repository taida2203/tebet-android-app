package com.tebet.mojual.view.signup

import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.view.base.BaseViewModel

class SignUpInfoViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<SignUpNavigator>(dataManager, schedulerProvider) {
    var userProfile: UserProfile = UserProfile()

    fun loadProfile() {
//        compositeDisposable.add(
//            dataManager.getProfile()
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(object : CallbackWrapper<UserProfile>() {
//                    override fun onSuccess(dataResponse: UserProfile) {
//                        if (dataResponse.status.equals("INIT")) {
//                        } else {
//                        }
//                    }
//
//                    override fun onFailure(error: String?) {
//                    }
//                })
//        )
    }
}