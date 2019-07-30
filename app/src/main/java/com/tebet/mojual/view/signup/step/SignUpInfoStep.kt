package com.tebet.mojual.view.signup

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.tebet.mojual.view.base.BaseFragment
import com.tebet.mojual.view.signup.step2.SignUpInfoStepViewModel

abstract class SignUpInfoStep<T : ViewDataBinding, V : SignUpInfoStepViewModel<*>> : BaseFragment<T, V>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.userProfile.set((activity as SignUpInfo).viewModel.userProfile)
    }
}