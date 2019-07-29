package com.tebet.mojual.view.signup.step2

import androidx.lifecycle.ViewModelProviders
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.databinding.FragmentSignUpInfoStep2Binding
import com.tebet.mojual.databinding.FragmentSignUpInfoStep3Binding
import com.tebet.mojual.view.base.BaseFragment

class SignUpInfoStep3 : BaseFragment<FragmentSignUpInfoStep3Binding, SignUpInfoStep3Model>() {
    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: SignUpInfoStep3Model
        get() = ViewModelProviders.of(this, factory).get(SignUpInfoStep3Model::class.java)

    override val layoutId: Int
        get() = R.layout.fragment_sign_up_info_step3
}