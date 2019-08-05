package com.tebet.mojual.view.signup.step

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.tebet.mojual.view.base.BaseFragment
import android.view.WindowManager
import br.com.ilhasoft.support.validation.Validator
import com.tebet.mojual.view.signup.SignUpInfo


abstract class SignUpInfoStep<T : ViewDataBinding, V : SignUpInfoStepViewModel<*>> : BaseFragment<T, V>() {
    protected lateinit var validator: Validator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.userProfile.set((activity as SignUpInfo).viewModel.userProfile)
        validator = Validator(viewDataBinding)
    }
    abstract fun validate(): Boolean
    override fun onCreate(savedInstanceState: Bundle?) {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        super.onCreate(savedInstanceState)

    }
}