package com.tebet.mojual.view.profile

import androidx.room.EmptyResultSetException
import co.sdk.auth.AuthSdk
import co.sdk.auth.core.models.ApiCallBack
import co.sdk.auth.core.models.LoginException
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.local.db.dao.UserProfileDao
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class ProfileViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<ProfileNavigator>(dataManager, schedulerProvider) {

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

    fun logout() {
        AuthSdk.instance.logout(true, object : ApiCallBack<Any>() {
            override fun onSuccess(responeCode: Int, response: Any?) {
                navigator.openLoginScreen()
            }

            override fun onFailed(exeption: LoginException) {
                navigator.openLoginScreen()
            }
        })
    }
}