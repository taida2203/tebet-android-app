package com.tebet.mojual.view.signup.step2

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.data.models.Address
import com.tebet.mojual.databinding.FragmentSignUpInfoStep2Binding
import com.tebet.mojual.view.signup.SignUpInfoStep

class SignUpInfoStep2 : SignUpInfoStep<FragmentSignUpInfoStep2Binding, SignUpInfoStep2Model>() {
    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: SignUpInfoStep2Model
        get() = ViewModelProviders.of(this, factory).get(SignUpInfoStep2Model::class.java)

    override val layoutId: Int
        get() = R.layout.fragment_sign_up_info_step2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.expandableView?.let {
            it.addView(ParentItem(it.context, "DOMICILE ADDRESS"))
            if (viewModel.userProfile.get()?.domicileAddress == null) viewModel.userProfile.get()?.domicileAddress = Address()
            if (viewModel.userProfile.get()?.pickupAddress == null) viewModel.userProfile.get()?.pickupAddress = Address()
            it.addView(ChildItem(it, viewModel.userProfile.get()?.domicileAddress ?: Address()))
            it.addView(ParentItem(it.context, "PICKUP ADDRESS"))
            it.addView(ChildItem(it, viewModel.userProfile.get()?.pickupAddress ?: Address()))
            it.expand(0)
        }
    }
}