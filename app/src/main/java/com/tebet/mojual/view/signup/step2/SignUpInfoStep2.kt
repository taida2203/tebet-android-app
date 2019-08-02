package com.tebet.mojual.view.signup.step2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.data.models.Address
import com.tebet.mojual.databinding.FragmentSignUpInfoStep2Binding
import com.tebet.mojual.view.signup.SignUpInfoStep
import com.tebet.mojual.view.signup.step2.address.ChildItem
import com.tebet.mojual.view.signup.step2.address.ParentItem
import com.tebet.mojual.view.signup.step2.map.GoogleMapActivity

class SignUpInfoStep2 : SignUpInfoStep<FragmentSignUpInfoStep2Binding, SignUpInfoStep2Model>(),
    SignUpInfoStep2Navigator {
    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: SignUpInfoStep2Model
        get() = ViewModelProviders.of(this, factory).get(SignUpInfoStep2Model::class.java)

    override val layoutId: Int
        get() = R.layout.fragment_sign_up_info_step2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.expandableView?.let {
            it.addView(ParentItem(it.context, "DOMICILE ADDRESS"))
            if (viewModel.userProfile.get()?.domicileAddress == null) viewModel.userProfile.get()?.domicileAddress =
                Address()
            if (viewModel.userProfile.get()?.pickupAddress == null) viewModel.userProfile.get()?.pickupAddress =
                Address()
            it.addView(
                ChildItem(
                    it,
                    viewModel.userProfile.get()?.domicileAddress ?: Address(),
                    viewModel
                )
            )
            it.addView(ParentItem(it.context, "PICKUP ADDRESS"))
            it.addView(
                ChildItem(
                    it,
                    viewModel.userProfile.get()?.pickupAddress ?: Address(),
                    viewModel
                )
            )
            it.expand(0)
        }
    }

    override fun selectLocation() {
        startActivityForResult(Intent(activity, GoogleMapActivity::class.java), 500)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            500 -> {
                if (resultCode == Activity.RESULT_OK) {
                    viewModel.address?.get()?.latitude = 21.035732
                    viewModel.address?.get()?.longitude = 105.8476363
                }
            }
        }
    }

}