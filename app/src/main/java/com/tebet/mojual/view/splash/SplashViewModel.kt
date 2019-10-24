package com.tebet.mojual.view.splash

import androidx.lifecycle.MutableLiveData
import co.sdk.auth.AuthSdk
import com.tebet.mojual.common.util.Sensor
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.NetworkError
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel

class SplashViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<SplashNavigator>(dataManager, schedulerProvider) {
    constructor(
        dataManager: DataManager,
        schedulerProvider: SchedulerProvider,
        sensorManager: Sensor
    ) : this(dataManager, schedulerProvider) {
        this.sensorManager = sensorManager
    }

    lateinit var sensorManager: Sensor
    var profileError: MutableLiveData<String> = MutableLiveData()
    fun loadProfile() {
        if (AuthSdk.instance.currentToken?.appToken == null) {
            navigator.openLoginScreen()
            return
        }
        compositeDisposable.add(
            sensorManager.connectIOTWifi()
                .concatMap { dataManager.getProfile() }
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : CallbackWrapper<UserProfile>() {
                    override fun onSuccess(dataResponse: UserProfile) {
                        when (dataResponse.statusEnum) {
                            UserProfile.Status.Init -> navigator.openSetPasswordScreen()
                            UserProfile.Status.InitProfile -> navigator.openSignUpInfoScreen()
                            else -> navigator.openHomeScreen()
                        }
                    }

                    override fun onFailure(error: NetworkError) {
                        navigator.openLoginScreen()
                        handleError(error)
                    }

                    override fun onComplete() {
                    }
                })
        )
    }
}