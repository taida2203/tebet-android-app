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
import kotlinx.android.synthetic.main.activity_sign_up_password.*

class SignUpPassword : BaseActivity() {
    override val contentLayoutId: Int
        get() = R.layout.activity_sign_up_password

    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
        btnNext.setOnClickListener {
            this@SignUpPassword.finish()
            startActivity(Intent(this@SignUpPassword, SignUpInfo::class.java))
        }
    }

}