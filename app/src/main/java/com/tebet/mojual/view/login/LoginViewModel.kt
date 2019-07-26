package com.tebet.mojual.view.login

import androidx.lifecycle.MutableLiveData
import co.sdk.auth.AuthSdk
import co.sdk.auth.core.LoginConfiguration
import co.sdk.auth.network.ServiceHelper
import com.tebet.mojual.data.remote.ApiInterface
import com.tebet.mojual.data.repository.ProfileRepository
import com.tebet.mojual.view.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LoginViewModel @Inject constructor(private var profileRepository: ProfileRepository) :
    BaseViewModel<LoginNavigator>() {
    var profileError: MutableLiveData<String> = MutableLiveData()

    fun onLoginClick() {
        navigator.openLoginScreen()
    }

    fun onRegistrationClick() {
        navigator.doAccountKitLogin(true)
    }

    fun loadProfile() {
        compositeDisposable.add(
            profileRepository.getProfile()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribe({ profileResponse ->
                    if (profileResponse.data?.status.equals("INIT")) {
                        navigator.openRegistrationScreen()
                    } else {
                        navigator.openHomeScreen()
                    }
                }, { e ->
                    navigator.openLoginScreen()
                })
        )
    }

    fun register() {
        compositeDisposable.add(
            profileRepository.registerFromApi(
                LoginConfiguration(
                    logoutWhileExpired = false,
                    token = AuthSdk.instance.getBrandLoginToken()?.token,
                    phone = AuthSdk.instance.getBrandLoginToken()?.phone
                )
            ).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribe({
                    navigator.doAccountKitLogin(false)
                }, {
                })
        )
    }

}