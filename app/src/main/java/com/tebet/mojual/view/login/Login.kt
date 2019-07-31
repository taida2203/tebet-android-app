package com.tebet.mojual.view.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import co.sdk.auth.AuthSdk
import co.sdk.auth.core.AuthAccountKitMethod
import co.sdk.auth.core.LoginConfiguration
import co.sdk.auth.core.models.ApiCallBack
import co.sdk.auth.core.models.LoginException
import co.sdk.auth.core.models.Token
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.databinding.ActivityLoginBinding
import com.tebet.mojual.view.home.HomeActivity
import com.tebet.mojual.view.loginpassword.LoginWithPassword
import com.tebet.mojual.view.registration.SignUpPassword
import com.tebet.mojual.view.base.BaseActivity
import com.tebet.mojual.view.forgotpassword.ForgotPassword

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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let { AuthSdk.instance.onActivityResult(requestCode, resultCode, it) }
        when (resultCode) {
            LOGIN_PASSWORD -> if (requestCode == Activity.RESULT_OK) finish()
        }
    }

    override fun openLoginScreen() {
        startActivityForResult(
            Intent(this, LoginWithPassword::class.java),
            LOGIN_PASSWORD
        )
    }

    override fun openHomeScreen() {
        startActivity(Intent(this, HomeActivity::class.java))
    }

    override fun openRegistrationScreen() {
        startActivity(Intent(this, SignUpPassword::class.java))
    }
}