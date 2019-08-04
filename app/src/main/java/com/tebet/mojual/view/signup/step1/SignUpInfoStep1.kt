package com.tebet.mojual.view.signup.step1

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.lifecycle.ViewModelProviders
import br.com.ilhasoft.support.validation.Validator
import co.common.view.dialog.DateDialog
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.databinding.FragmentSignUpInfoStep1Binding
import com.tebet.mojual.view.signup.SignUpInfo
import com.tebet.mojual.view.signup.step.SignUpInfoStep
import java.util.*

class SignUpInfoStep1 : SignUpInfoStep<FragmentSignUpInfoStep1Binding, SignUpInfoStep1Model>(),
    SignUpInfoStep1Navigator {
    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: SignUpInfoStep1Model
        get() = ViewModelProviders.of(this, factory).get(SignUpInfoStep1Model::class.java)

    override val layoutId: Int
        get() = R.layout.fragment_sign_up_info_step1
    lateinit var validator: Validator


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navigator = (activity as SignUpInfo)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        validator = Validator(viewDataBinding)
    }

    override fun captureAvatar() {
    }

    override fun captureEKTP() {
    }

    override fun validate(): Boolean {
        return validator.validate(
            Arrays.asList(
                viewDataBinding?.fullName,
                viewDataBinding?.birthday,
                viewDataBinding?.ktpNumber
            )
        )
    }

    override fun selectBirthDay() {
    }
}