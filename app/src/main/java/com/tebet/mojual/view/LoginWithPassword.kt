package com.tebet.mojual.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import co.sdk.auth.AuthSdk
import co.sdk.auth.core.AuthAccountKitMethod
import co.sdk.auth.core.AuthPasswordMethod
import co.sdk.auth.core.LoginConfiguration
import co.sdk.auth.core.models.ApiCallBack
import co.sdk.auth.core.models.LoginException
import co.sdk.auth.core.models.Token
import com.tebet.mojual.R
import com.tebet.mojual.common.base.BaseActivity
import kotlinx.android.synthetic.main.activity_login_password.*

class LoginWithPassword : BaseActivity() {
    override val contentLayoutId: Int
        get() = R.layout.activity_login_password

    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
        title = "Login with phone number"
//        navLayout.visibility = View.VISIBLE
        btnNext.setOnClickListener {
            val configuration = LoginConfiguration(false)
            configuration.username = etPhone.text.toString()
            configuration.password = etPassword.text.toString()
            showLoading(true)
            AuthSdk.instance.login(this@LoginWithPassword, AuthPasswordMethod(), configuration, object :
                ApiCallBack<Token>() {
                override fun onSuccess(responeCode: Int, response: Token?) {
                    showLoading(false)
                    setResult(Activity.RESULT_OK)
                    this@LoginWithPassword.finish()
                    startActivity(Intent(this@LoginWithPassword, HomeActivity::class.java))
                }

                override fun onFailed(exeption: LoginException) {
                    showLoading(false)
                    handleError(exeption)
                }
            })
        }
        btnForgotPassword.setOnClickListener {
            showLoading(true)
            AuthSdk.instance.login(this, AuthAccountKitMethod(), LoginConfiguration(false), object : ApiCallBack<Token>() {
                override fun onSuccess(responeCode: Int, response: Token?) {
                    showLoading(false)
                    finish()
                    startActivity(Intent(this@LoginWithPassword, ForgotPassword::class.java))
                }

                override fun onFailed(exeption: LoginException) {
                    showLoading(false)
                    handleError(exeption)
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        showLoading(false)
        data?.let { AuthSdk.instance.onActivityResult(requestCode, resultCode, it) }
    }

}
