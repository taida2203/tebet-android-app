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
import com.tebet.mojual.view.signup.step.SignUpInfoStep
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

    var addressViews = ArrayList<ChildItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.expandableView?.let {
            it.addView(ParentItem(it.context, "DOMICILE ADDRESS", true))
            if (viewModel.userProfile.get()?.domicileAddress == null) viewModel.userProfile.get()?.domicileAddress =
                Address()
            if (viewModel.userProfile.get()?.pickupAddress == null) viewModel.userProfile.get()?.pickupAddress =
                Address()
            addressViews.add(
                ChildItem(
                    it,
                    viewModel.userProfile.get()?.domicileAddress ?: Address(),
                    viewModel
                )
            )
            it.addView(addressViews[0])
            it.addView(ParentItem(it.context, "PICKUP ADDRESS"))
            addressViews.add(
                ChildItem(
                    it,
                    viewModel.userProfile.get()?.pickupAddress ?: Address(),
                    viewModel
                )
            )
            it.addView(addressViews[1])
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
                    val dataReturn = data?.getParcelableExtra<Address>("LOCATION")
                    viewModel.address?.set(dataReturn)
                }
            }
        }
    }

    override fun validate(): Boolean {
        return addressViews.firstOrNull { address -> !address.validate() }?.validate() ?: true
    }
}