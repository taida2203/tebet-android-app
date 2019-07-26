package com.tebet.mojual.view.splash

import androidx.lifecycle.MutableLiveData
import co.sdk.auth.AuthSdk
import com.tebet.mojual.App
import com.tebet.mojual.R
import com.tebet.mojual.common.util.checkConnectivity
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.view.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject

class SplashViewModel constructor(dataManager: DataManager) : BaseViewModel<SplashNavigator>(dataManager) {
    var profileError: MutableLiveData<String> = MutableLiveData()
    fun loadProfile() {
        if (App.instance.checkConnectivity()) {
            if (AuthSdk.instance.currentToken?.appToken != null) {
                compositeDisposable.add(
                    dataManager.getProfile()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .debounce(400, MILLISECONDS)
                        .subscribe({ profileResponse ->
                            if (profileResponse.data?.status.equals("INIT")) {
                                navigator.openSetPasswordScreen()
                            } else {
                                navigator.openHomeScreen()
                            }
                        }, { e ->
                            navigator.openLoginScreen()
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