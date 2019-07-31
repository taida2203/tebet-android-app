package com.tebet.mojual.view.splash

import androidx.lifecycle.MutableLiveData
import androidx.room.EmptyResultSetException
import co.sdk.auth.AuthSdk
import com.tebet.mojual.App
import com.tebet.mojual.R
import com.tebet.mojual.common.util.checkConnectivity
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.view.base.BaseViewModel
import io.reactivex.observers.DisposableObserver

class SplashViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) : BaseViewModel<SplashNavigator>(dataManager, schedulerProvider) {
    var profileError: MutableLiveData<String> = MutableLiveData()
    fun loadProfile() {
        if (App.instance.checkConnectivity()) {
            if (AuthSdk.instance.currentToken?.appToken != null) {
                compositeDisposable.add(
                    dataManager.getProfile()
                        .concatMap { profileResponse ->
                            dataManager.insertUserProfile(profileResponse.data)
                        }.concatMap {
                            dataManager.userProfile
                        }.concatMap { userProfileDao ->
                            userProfileDao.queryUserProfile().doOnError { message -> EmptyResultSetException("") }
                                .toObservable()
                        }
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribeWith(object : DisposableObserver<UserProfile>() {
                            override fun onComplete() {
                            }

                            override fun onNext(dataResponse: UserProfile) {
                                if (dataResponse.status.equals("INIT")) {
                                    navigator.openSetPasswordScreen()
                                } else {
                                    navigator.openHomeScreen()
                                }
                            }

                            override fun onError(e: Throwable) {
                                navigator.openLoginScreen()
                            }
                        })
                )
            } else {
                navigator.openLoginScreen()
            }
        } else {
            profileError.postValue(App.instance.getString(R.string.general_message_error))
        }
    }
}