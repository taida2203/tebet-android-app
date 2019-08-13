package com.tebet.mojual.view.loginpassword

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import br.com.ilhasoft.support.validation.Validator
import co.sdk.auth.AuthSdk
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.databinding.ActivityLoginPasswordBinding
import com.tebet.mojual.databinding.ItemHomeIconBinding
import com.tebet.mojual.view.forgotpassword.ForgotPassword
import com.tebet.mojual.view.home.HomeActivity
import com.tebet.mojual.view.base.BaseActivity
import com.tebet.mojual.view.registration.SignUpPassword
import com.tebet.mojual.view.signup.SignUpInfo

class LoginWithPassword : BaseActivity<ActivityLoginPasswordBinding, LoginWithPasswordViewModel>(),
    LoginWithPasswordNavigator {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val viewModel: LoginWithPasswordViewModel
        get() = ViewModelProviders.of(this, factory).get(LoginWithPasswordViewModel::class.java)

    override val contentLayoutId: Int
        get() = R.layout.activity_login_password

    private lateinit var validator: Validator
    private var topRightViewBinding: ItemHomeIconBinding? = null

    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
        viewModel.navigator = this
        validator = Validator(viewDataBinding)
        topRightViewBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_home_icon, baseBinding.topRightHolder, true)
        title = "Login with phone number"
        viewModel.loadData()
        viewDataBinding?.snCountryFlag?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.userInputPhonePrefix = viewModel.items[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
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
        setResult(Activity.RESULT_OK)
        finish()
        startActivity(Intent(this, ForgotPassword::class.java))
    }

    override fun openRegistrationScreen() {
        finish()
        startActivity(Intent(this, SignUpPassword::class.java))
    }

    override fun openSignUpInfoScreen() {
        finish()
        startActivity(Intent(this, SignUpInfo::class.java))
    }

    override fun dataValid(): Boolean {
        return validator.validate()
    }
}
