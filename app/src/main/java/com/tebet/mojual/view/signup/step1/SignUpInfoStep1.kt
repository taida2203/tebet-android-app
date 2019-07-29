package com.tebet.mojual.view.signup.step1

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.databinding.FragmentSignUpInfoStep1Binding
import com.tebet.mojual.view.base.BaseFragment
import com.tebet.mojual.view.signup.SignUpInfo

class SignUpInfoStep1 : BaseFragment<FragmentSignUpInfoStep1Binding, SignUpInfoStep1Model>(), SignUpInfoStep1Navigator {
    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: SignUpInfoStep1Model
        get() = ViewModelProviders.of(this, factory).get(SignUpInfoStep1Model::class.java)

    override val layoutId: Int
        get() = R.layout.fragment_sign_up_info_step1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navigator = (activity as SignUpInfo)
    }

    override fun captureAvatar() {
    }

    override fun captureEKTP() {
    }

}