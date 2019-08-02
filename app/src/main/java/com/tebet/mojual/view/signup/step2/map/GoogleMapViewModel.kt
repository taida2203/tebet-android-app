package com.tebet.mojual.view.signup.step2.map

import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.EmptyResponse
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseActivityNavigator
import com.tebet.mojual.view.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class GoogleMapViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<BaseActivityNavigator>(dataManager, schedulerProvider) {
    var userProfile: UserProfile = UserProfile()

    fun updateUserProfile() {
        navigator.showLoading(true)
        compositeDisposable.add(
            dataManager.updateProfile(userProfile)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribeWith(object : CallbackWrapper<EmptyResponse>() {
                    override fun onSuccess(dataResponse: EmptyResponse) {
                    }

                    override fun onFailure(error: String?) {
                        handleError(error)
                    }

                    override fun onComplete() {
                        super.onComplete()
                        navigator.showLoading(false)
                    }
                })
        )
    }
}