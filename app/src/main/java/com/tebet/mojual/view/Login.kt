package com.tebet.mojual.view

import android.content.Intent
import android.os.Bundle
import co.sdk.auth.AuthSdk
import co.sdk.auth.core.AuthAccountKitMethod
import co.sdk.auth.core.LoginConfiguration
import co.sdk.auth.core.models.ApiCallBack
import co.sdk.auth.core.models.LoginException
import co.sdk.auth.core.models.Token
import com.tebet.mojual.R
import com.tebet.mojual.common.base.BaseActivity
import kotlinx.android.synthetic.main.activity_login.*

class Login : BaseActivity() {
    override val contentLayoutId: Int
        get() = R.layout.activity_login

    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
        btnLogin.setOnClickListener {
            AuthSdk.init(this, "http://192.168.4.1", "", "", "")

            AuthSdk.instance.login(this, AuthAccountKitMethod(), LoginConfiguration(), object : ApiCallBack<Token>() {
                override fun onSuccess(responeCode: Int, response: Token?) {
                }

                override fun onFailed(exeption: LoginException) {
                }
            })
        }
        btnRegistration.setOnClickListener {
            startActivity(Intent(this@Login, SignUpInfo::class.java))
        }
    }

}