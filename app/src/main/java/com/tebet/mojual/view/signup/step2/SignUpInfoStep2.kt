package com.tebet.mojual.view.signup.step2

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.databinding.FragmentSignUpInfoStep2Binding
import com.tebet.mojual.view.base.BaseFragment
import com.tebet.mojual.view.signup.SignUpInfoStep

class SignUpInfoStep2 : SignUpInfoStep<FragmentSignUpInfoStep2Binding, SignUpInfoStep2Model>() {
    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: SignUpInfoStep2Model
        get() = ViewModelProviders.of(this, factory).get(SignUpInfoStep2Model::class.java)

    override val layoutId: Int
        get() = R.layout.fragment_sign_up_info_step2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}