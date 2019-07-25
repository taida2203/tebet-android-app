package com.tebet.mojual.view.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import co.sdk.auth.AuthSdk
import com.tebet.mojual.BR
//import com.tebet.mojual.di.module.LoginModule
import com.tebet.mojual.R
import com.tebet.mojual.ViewModelProviderFactory
import com.tebet.mojual.databinding.ActivityLoginBinding
import com.tebet.mojual.view.LoginWithPassword
import com.tebet.mojual.view.SignUpPassword
import com.tebet.mojual.view.base.BaseActivityNew
import javax.inject.Inject

class Login : BaseActivityNew<ActivityLoginBinding, LoginViewModel>(), LoginNavigator {
    @Inject
    lateinit var factory: ViewModelProviderFactory
    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: LoginViewModel
        get() = ViewModelProviders.of(this, factory).get(LoginViewModel::class.java)

    override fun openLoginScreen() {
        startActivityForResult(Intent(this@Login, LoginWithPassword::class.java),
            LOGIN_PASSWORD
        )
    }

    override fun openRegistrationScreen() {
        startActivity(Intent(this@Login, SignUpPassword::class.java))
    }

    companion object {
        var LOGIN_PASSWORD = 1991
    }

    override val contentLayoutId: Int
        get() = R.layout.activity_login

    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
        //Inside your activity's onCreate:
//        DaggerLoginComponent.builder()
//            .loginModule(LoginModule(this))
//            .build()
//            .inject(this)
        viewModel.navigator = this
//        title = "Login with phone number"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        showLoading(false)
        data?.let { AuthSdk.instance.onActivityResult(requestCode, resultCode, it) }
        when (resultCode) {
            LOGIN_PASSWORD -> if (requestCode == Activity.RESULT_OK) finish()
        }
    }
}