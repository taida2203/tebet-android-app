package com.tebet.mojual.view.signup.step0

import android.content.Intent
import android.os.Bundle
import com.tebet.mojual.R
import com.tebet.mojual.view.forgotpassword.ForgotPassword
import com.tebet.mojual.view.signup.SignUpInfo

class SignUpPassword : ForgotPassword() {
    override val contentLayoutId: Int
        get() = R.layout.activity_sign_up_password

    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
        super.onCreateBase(savedInstanceState, layoutId)
        title = getString(R.string.sign_up_password_title)
    }

    override fun openHomeScreen() {
        finish()
        startActivity(Intent(this, SignUpInfo::class.java))
    }
}
