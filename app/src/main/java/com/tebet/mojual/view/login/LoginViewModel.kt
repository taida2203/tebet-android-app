package com.tebet.mojual.view.login

import androidx.lifecycle.MutableLiveData
import co.sdk.auth.AuthSdk
import com.tebet.mojual.view.base.BaseViewModel
import javax.inject.Inject

class LoginViewModel @Inject constructor() : BaseViewModel<LoginNavigator>() {
    var profileError: MutableLiveData<String> = MutableLiveData()

    fun onLoginClick() {
        navigator.openLoginScreen()
    }

    fun onRegistrationClick() {
//        AuthSdk.instance.login(loginActivity, AuthAccountKitMethod(), LoginConfiguration(logoutWhileExpired = false), object : ApiCallBack<Token>() {
//            override fun onSuccess(responeCode: Int, response: Token?) {
//                showLoading(true)
//                    ServiceHelper.createService(ApiInterface::class.java).getProfile()
//                        .enqueue(object : retrofit2.Callback<AuthJson<UserProfile>> {
//                            override fun onResponse(
//                                call: Call<AuthJson<UserProfile>>,
//                                response: Response<AuthJson<UserProfile>>
//                            ) {
//                                showLoading(false)
//                                if (response.body()?.data?.status.equals("INIT")) {
//                                    startActivity(Intent(this@Login, SignUpPassword::class.java))
//                                } else {
//                                    startActivity(Intent(this@Login, HomeActivity::class.java))
//                                }
//                                finish()
//                            }
//
//                            override fun onFailure(call: Call<AuthJson<UserProfile>>, t: Throwable) {
//                                showLoading(false)
//                                handleError(t)
//                            }
//                        })
//            }
//
//            override fun onFailed(exeption: LoginException) {
//                if(exeption.errorCode == 502) return
//                val config = LoginConfiguration(false)
//                config.token = AuthSdk.instance.getBrandLoginToken()?.token
//                config.phone = AuthSdk.instance.getBrandLoginToken()?.phone
//
//                ServiceHelper.createService(ApiInterface::class.java).register(config)
//                    .enqueue(object : retrofit2.Callback<ResponseBody> {
//                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
////                            showLoading(false)
////                            handleError(t)
//                        }
//
//                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                            AuthSdk.instance.login(
//                                loginActivity,
//                                AuthAccountKitMethod(),
//                                LoginConfiguration(false),
//                                object : ApiCallBack<Token>() {
//                                    override fun onSuccess(responeCode: Int, response: Token?) {
////                                        showLoading(false)
//                                        navigator.openRegistrationScreen()
//                                    }
//
//                                    override fun onFailed(exeption: LoginException) {
////                                        showLoading(false)
////                                        handleError(exeption)
//                                    }
//                                })
//                        }
//                    })
//            }
//        })
    }

}