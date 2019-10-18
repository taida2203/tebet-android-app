package com.tebet.mojual.view.loginpassword

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import br.com.ilhasoft.support.validation.Validator
import co.sdk.auth.AuthSdk
import androidx.databinding.library.baseAdapters.BR
import com.tebet.mojual.R
import com.tebet.mojual.databinding.ActivityLoginPasswordBinding
import com.tebet.mojual.databinding.ItemHomeIconBinding
import com.tebet.mojual.view.forgotpassword.ForgotPassword
import com.tebet.mojual.view.home.Home
import com.tebet.mojual.view.base.BaseActivity
import com.tebet.mojual.view.signup.step0.SignUpPassword
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
        validator.enableFormValidationMode()
        topRightViewBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_home_icon, baseBinding?.topRightHolder, true)
        title = getString(R.string.login_password_title)
        viewModel.loadData()
        viewDataBinding?.snCountryFlag?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.userInputPhonePrefix = viewModel.items[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        viewDataBinding?.iconContainerPhone?.viewTreeObserver?.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewDataBinding?.iconContainerPhone?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                    val params = viewDataBinding?.iconContainerPassword?.layoutParams
                    params?.width = viewDataBinding?.iconContainerPhone?.width
                    viewDataBinding?.iconContainerPassword?.layoutParams = params
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        AuthSdk.instance.onActivityResult(requestCode, resultCode, data)
    }

    override fun openHomeScreen() {
        setResult(Activity.RESULT_OK)
        finish()
        startActivity(Intent(this, Home::class.java))
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
        hideKeyboard()
        return validator.validate()
    }
}
