package com.tebet.mojual.view.loginpassword

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import br.com.ilhasoft.support.validation.Validator
import co.sdk.auth.AuthSdk
import co.sdk.auth.core.AuthAccountKitMethod
import co.sdk.auth.core.AuthPasswordMethod
import co.sdk.auth.core.LoginConfiguration
import co.sdk.auth.core.models.ApiCallBack
import co.sdk.auth.core.models.LoginException
import co.sdk.auth.core.models.Token
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.databinding.ActivityLoginPasswordBinding
import com.tebet.mojual.view.forgotpassword.ForgotPassword
import com.tebet.mojual.view.home.HomeActivity
import com.tebet.mojual.view.base.BaseActivity
import com.tebet.mojual.view.registration.SignUpPassword

class LoginWithPassword : BaseActivity<ActivityLoginPasswordBinding, LoginWithPasswordViewModel>(),
    LoginWithPasswordNavigator {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val viewModel: LoginWithPasswordViewModel
        get() = ViewModelProviders.of(this, factory).get(LoginWithPasswordViewModel::class.java)

    override val contentLayoutId: Int
        get() = R.layout.activity_login_password

    private lateinit var validator: Validator

    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
        viewModel.navigator = this
        validator = Validator(viewDataBinding)
        title = "Login with phone number"
//        navLayout.visibility = View.VISIBLE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let { AuthSdk.instance.onActivityResult(requestCode, resultCode, it) }
    }

    override fun openHomeScreen() {
        setResult(Activity.RESULT_OK)
        finish()
        startActivity(Intent(this, HomeActivity::class.java))
    }

    override fun openForgotPasswordScreen() {
        finish()
        startActivity(Intent(this, ForgotPassword::class.java))
    }

    override fun openRegistrationScreen() {
        finish()
        startActivity(Intent(this, SignUpPassword::class.java))
    }

    override fun dataValid(): Boolean {
        return validator.validate()
    }
}
