package com.tebet.mojual.view.profilechangepass

import android.os.Bundle
import com.tebet.mojual.R
import com.tebet.mojual.view.forgotpassword.ForgotPassword

class ChangePassword : ForgotPassword() {
    override val contentLayoutId: Int
        get() = R.layout.activity_sign_up_password

    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
        super.onCreateBase(savedInstanceState, layoutId)
        title = getString(R.string.change_pass_title)
        enableBackButton = true
    }

    override fun openHomeScreen() {
        finish()
    }
}
