package com.tebet.mojual

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import co.squline.sdk.auth.AuthSdk
import co.squline.sdk.auth.core.AuthAccountKitMethod
import co.squline.sdk.auth.core.LoginConfiguration
import co.squline.sdk.auth.core.models.ApiCallBack
import co.squline.sdk.auth.core.models.LoginException
import co.squline.sdk.auth.core.models.Token

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AuthSdk.init(this, "", "", "", "")
        AuthSdk.instance.login(this, AuthAccountKitMethod(), LoginConfiguration(), object : ApiCallBack<Token>() {
            override fun onSuccess(responeCode: Int, response: Token?) {
            }

            override fun onFailed(exeption: LoginException) {
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let { AuthSdk.instance.onActivityResult(requestCode, resultCode, it) }
    }
}
