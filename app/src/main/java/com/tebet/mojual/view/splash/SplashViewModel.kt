package com.tebet.mojual.view.splash

import androidx.lifecycle.MutableLiveData
import co.sdk.auth.AuthSdk
import co.sdk.auth.core.models.AuthJson
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import io.reactivex.observers.DisposableObserver

class SplashViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) : BaseViewModel<SplashNavigator>(dataManager, schedulerProvider) {
    var profileError: MutableLiveData<String> = MutableLiveData()
    fun loadProfile() {
        if (AuthSdk.instance.currentToken?.appToken == null) {
            navigator.openLoginScreen()
            return
        }
        compositeDisposable.add(
            dataManager.getProfile()
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : CallbackWrapper<UserProfile>() {
                    override fun onSuccess(dataResponse: UserProfile) {
                        if (dataResponse.status.equals("INIT")) {
                            navigator.openSetPasswordScreen()
                        } else {
                            navigator.openHomeScreen()
                        }
                    }

                    override fun onFailure(error: String?) {
                        navigator.openLoginScreen()
                    }

                    override fun onComplete() {
                    }
                })
        )
    }
}