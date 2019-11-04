package com.tebet.mojual.view.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import co.sdk.auth.AuthSdk
import androidx.databinding.library.baseAdapters.BR
import com.tebet.mojual.R
import com.tebet.mojual.databinding.ActivityLoginBinding
import com.tebet.mojual.view.home.Home
import com.tebet.mojual.view.loginpassword.LoginWithPassword
import com.tebet.mojual.view.signup.step0.SignUpPassword
import com.tebet.mojual.view.base.BaseActivity
import com.tebet.mojual.view.signup.SignUpInfo

class Login : BaseActivity<ActivityLoginBinding, LoginViewModel>(), LoginNavigator {
    companion object {
        var LOGIN_PASSWORD = 1991
    }

    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: LoginViewModel
        get() = ViewModelProviders.of(this, factory).get(LoginViewModel::class.java)

    override val contentLayoutId: Int
        get() = R.layout.activity_login

    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
        viewModel.navigator = this
        enableBackButton = false
        viewDataBinding?.btnRegistration?.setBackgroundResource(R.drawable.rounded_bg_button_brown)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        hideKeyboard()
        AuthSdk.instance.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            LOGIN_PASSWORD -> if (resultCode == Activity.RESULT_OK) finish()
        }
    }

    override fun openLoginScreen() {
        startActivityForResult(
            Intent(this, LoginWithPassword::class.java),
            LOGIN_PASSWORD
        )
    }

    override fun openSignUpInfoScreen() {
        finish()
        startActivity(Intent(this, SignUpInfo::class.java))
    }


    override fun openHomeScreen() {
        finish()
        startActivity(Intent(this, Home::class.java))
    }

    override fun openRegistrationScreen() {
        finish()
        startActivity(Intent(this, SignUpPassword::class.java))
    }
}