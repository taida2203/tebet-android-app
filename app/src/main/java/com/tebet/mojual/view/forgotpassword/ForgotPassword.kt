package com.tebet.mojual.view.forgotpassword

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import br.com.ilhasoft.support.validation.Validator
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.databinding.ActivitySignUpPasswordBinding
import com.tebet.mojual.databinding.ItemHomeIconBinding
import com.tebet.mojual.view.home.Home
import com.tebet.mojual.view.base.BaseActivity

open class ForgotPassword : BaseActivity<ActivitySignUpPasswordBinding, ForgotPasswordViewModel>(),
    ForgotPasswordNavigator {
    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: ForgotPasswordViewModel
        get() = ViewModelProviders.of(this, factory).get(ForgotPasswordViewModel::class.java)

    override val contentLayoutId: Int
        get() = R.layout.activity_sign_up_password

    private lateinit var validator: Validator
    private var topRightViewBinding: ItemHomeIconBinding? = null

    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
        viewModel.navigator = this
        enableBackButton = false
        validator = Validator(viewDataBinding)
        validator.enableFormValidationMode()
        title = getString(R.string.forgot_password_title)
        topRightViewBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_home_icon, baseBinding.topRightHolder, true)
    }

    override fun openHomeScreen() {
        finish()
        startActivity(Intent(this, Home::class.java))
    }

    override fun dataValid(): Boolean {
        hideKeyboard()
        return validator.validate()
    }
}
