package com.tebet.mojual.view.signup.step2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.library.baseAdapters.BR
import com.tebet.mojual.R
import com.tebet.mojual.data.models.Address
import com.tebet.mojual.databinding.FragmentSignUpInfoStep2Binding
import com.tebet.mojual.view.signup.step.SignUpInfoStep
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
        viewModel.loadData()
    }

    override fun selectLocation(get: Address?) {
        var intent = Intent(activity, GoogleMapActivity::class.java)
        intent.putExtra("LOCATION", get)
        startActivityForResult(intent, 500)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            500 -> {
                if (resultCode == Activity.RESULT_OK) {
                    val dataReturn: Address? = try {
                        data?.getSerializableExtra("LOCATION") as Address
                    } catch (e: Exception) {
                    } as Address
                    when (dataReturn?.type) {
                        Address.DOMICILE_ADDRESS -> viewModel.userProfile.get()?.domicileAddress = dataReturn
                        Address.PICK_UP_ADDRESS -> viewModel.userProfile.get()?.pickupAddress = dataReturn
                    }
                }
            }
        }
    }

    override fun validate(): Boolean {
        return (if (!viewModel.userProfile.get()?.email.isNullOrBlank()) validator.validate() else true)
                && viewDataBinding?.llDomicileAddress?.validate() ?: true
                && viewDataBinding?.llPickupAddress?.validate() ?: true
    }
}