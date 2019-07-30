package com.tebet.mojual.view.signup.step1

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.databinding.FragmentSignUpInfoStep1Binding
import com.tebet.mojual.view.signup.SignUpInfo
import com.tebet.mojual.view.signup.SignUpInfoStep

class SignUpInfoStep1 : SignUpInfoStep<FragmentSignUpInfoStep1Binding, SignUpInfoStep1Model>(), SignUpInfoStep1Navigator {
    private var imagePath: String? = null
    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: SignUpInfoStep1Model
        get() = ViewModelProviders.of(this, factory).get(SignUpInfoStep1Model::class.java)

    override val layoutId: Int
        get() = R.layout.fragment_sign_up_info_step1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navigator = (activity as SignUpInfo)
        (activity as SignUpInfo).cameraCaptureData.observe(this, Observer<String> {
            imagePath = it
            viewModel.uploadImage(imagePath)
        })
    }

    override fun captureAvatar() {
    }

    override fun captureEKTP() {
    }
}