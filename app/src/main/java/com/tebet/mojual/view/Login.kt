package com.tebet.mojual.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import co.sdk.auth.AuthSdk
import co.sdk.auth.core.AuthAccountKitMethod
import co.sdk.auth.core.LoginConfiguration
import co.sdk.auth.core.models.ApiCallBack
import co.sdk.auth.core.models.AuthJson
import co.sdk.auth.core.models.LoginException
import co.sdk.auth.core.models.Token
import co.sdk.auth.network.ServiceHelper
import com.tebet.mojual.R
import com.tebet.mojual.common.base.BaseActivity
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.remote.ApiInterface
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class Login : BaseActivity() {
    companion object {
        var LOGIN_PASSWORD = 1991
    }

    override val contentLayoutId: Int
        get() = R.layout.activity_login

    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
        title = "Login with phone number"
        baseToolbar.visibility = View.GONE
        btnLogin.background = ContextCompat.getDrawable(this, R.drawable.rounded_btn_grey)
        btnLogin.setOnClickListener {
            startActivityForResult(Intent(this@Login, LoginWithPassword::class.java), LOGIN_PASSWORD)
        }
        btnRegistration.setOnClickListener {
            showLoading(true)
            AuthSdk.instance.login(this, AuthAccountKitMethod(), LoginConfiguration(logoutWhileExpired = false), object : ApiCallBack<Token>() {
                override fun onSuccess(responeCode: Int, response: Token?) {
                    showLoading(true)
                    ServiceHelper.createService(ApiInterface::class.java).getProfile()
                        .enqueue(object : retrofit2.Callback<AuthJson<UserProfile>> {
                            override fun onResponse(
                                call: Call<AuthJson<UserProfile>>,
                                response: Response<AuthJson<UserProfile>>
                            ) {
                                showLoading(false)
                                if (response.body()?.data?.status.equals("INIT")) {
                                    startActivity(Intent(this@Login, SignUpPassword::class.java))
                                } else {
                                    startActivity(Intent(this@Login, HomeActivity::class.java))
                                }
                                finish()
                            }

                            override fun onFailure(call: Call<AuthJson<UserProfile>>, t: Throwable) {
                                showLoading(false)
                                handleError(t)
                            }
                        })
                }

                override fun onFailed(exeption: LoginException) {
                    if(exeption.errorCode == 502) return
                    val config = LoginConfiguration(false)
                    config.token = AuthSdk.instance.getBrandLoginToken()?.token
                    config.phone = AuthSdk.instance.getBrandLoginToken()?.phone

                    ServiceHelper.createService(ApiInterface::class.java).register(config)
                        .enqueue(object : retrofit2.Callback<ResponseBody> {
                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                showLoading(false)
                                handleError(t)
                            }

                            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                                AuthSdk.instance.login(
                                    this@Login,
                                    AuthAccountKitMethod(),
                                    LoginConfiguration(false),
                                    object : ApiCallBack<Token>() {
                                        override fun onSuccess(responeCode: Int, response: Token?) {
                                            showLoading(false)
                                            startActivity(Intent(this@Login, SignUpPassword::class.java))
                                        }

                                        override fun onFailed(exeption: LoginException) {
                                            showLoading(false)
                                            handleError(exeption)
                                        }
                                    })
                            }
                        })
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        showLoading(false)
        data?.let { AuthSdk.instance.onActivityResult(requestCode, resultCode, it) }
        when (resultCode) {
            LOGIN_PASSWORD -> if (requestCode == Activity.RESULT_OK) finish()
        }
    }

}