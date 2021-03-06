package com.tebet.mojual.view.signup.step

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.tebet.mojual.view.base.BaseFragment
import android.view.WindowManager
import androidx.lifecycle.Observer
import br.com.ilhasoft.support.validation.Validator
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.view.signup.SignUpInfo


abstract class SignUpInfoStep<T : ViewDataBinding, V : SignUpInfoStepViewModel<*>> : BaseFragment<T, V>() {
    protected lateinit var validator: Validator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity is SignUpInfo) {
            viewModel.userProfile.set((activity as SignUpInfo).viewModel.userProfile)
            (activity as SignUpInfo).viewModel.userProfileLiveData.observe(this, Observer<UserProfile> { userProfile ->
                viewModel.userProfile.set(userProfile)
            })
        }
        validator = Validator(viewDataBinding)
        validator.enableFormValidationMode()
    }

    abstract fun validate(): Boolean
    override fun onCreate(savedInstanceState: Bundle?) {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        super.onCreate(savedInstanceState)

    }
}