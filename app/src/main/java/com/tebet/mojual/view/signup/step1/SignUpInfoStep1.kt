package com.tebet.mojual.view.signup.step1

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewTreeObserver
import androidx.lifecycle.ViewModelProviders
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.databinding.FragmentSignUpInfoStep1Binding
import com.tebet.mojual.view.signup.SignUpInfo
import com.tebet.mojual.view.signup.step.SignUpInfoStep

class SignUpInfoStep1 : SignUpInfoStep<FragmentSignUpInfoStep1Binding, SignUpInfoStep1Model>(),
    SignUpInfoStep1Navigator {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.ktpHolder?.viewTreeObserver?.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewDataBinding?.ktpHolder?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                    val params = viewDataBinding?.ktp?.layoutParams
                    params?.width = viewDataBinding?.ktpHolder?.width
                    params?.height = viewDataBinding?.ktpHolder?.height
                    viewDataBinding?.ktp?.layoutParams = params
                }
            })
    }

    override fun captureAvatar() {
    }

    override fun captureEKTP() {
    }

    override fun validate(): Boolean {
        return validator.validate(listOf(viewDataBinding?.fullName, viewDataBinding?.birthday, viewDataBinding?.ktpNumber))
                && (!TextUtils.isEmpty(viewModel.userProfile.get()?.ktpLocal) || !TextUtils.isEmpty(viewModel.userProfile.get()?.ktp)) &&
                (!TextUtils.isEmpty(viewModel.userProfile.get()?.avatarLocal) || !TextUtils.isEmpty(viewModel.userProfile.get()?.avatar))
    }

    override fun selectBirthDay() {
    }
}