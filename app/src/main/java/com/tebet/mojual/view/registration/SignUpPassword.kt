package com.tebet.mojual.view.registration

import android.os.Bundle
import com.tebet.mojual.R
import com.tebet.mojual.view.forgotpassword.ForgotPassword

class SignUpPassword : ForgotPassword() {
    override val contentLayoutId: Int
        get() = R.layout.activity_sign_up_password

    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
        viewModel.navigator = this
        title = "Sign Up"
    }
}
