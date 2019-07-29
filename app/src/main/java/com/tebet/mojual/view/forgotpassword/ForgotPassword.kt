package com.tebet.mojual.view.forgotpassword

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.databinding.ActivitySignUpPasswordBinding
import com.tebet.mojual.view.home.HomeActivity
import com.tebet.mojual.view.base.BaseActivity

open class ForgotPassword : BaseActivity<ActivitySignUpPasswordBinding, ForgotPasswordViewModel>(), ForgotPasswordNavigator {
    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: ForgotPasswordViewModel
        get() = ViewModelProviders.of(this, factory).get(ForgotPasswordViewModel::class.java)

    override val contentLayoutId: Int
        get() = R.layout.activity_sign_up_password

    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
        viewModel.navigator = this
        title = "Input new password"
    }

    override fun openHomeScreen() {
        this@ForgotPassword.finish()
        startActivity(Intent(this@ForgotPassword, HomeActivity::class.java))
    }

    override fun forgotPassword() {
        viewModel.forgotPassword(viewDataBinding?.tvPassword)
    }
}
