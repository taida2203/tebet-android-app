package com.tebet.mojual.view.signup.step3

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.data.models.Bank
import com.tebet.mojual.databinding.FragmentSignUpInfoStep3Binding
import com.tebet.mojual.view.signup.step.SignUpInfoStep

class SignUpInfoStep3 : SignUpInfoStep<FragmentSignUpInfoStep3Binding, SignUpInfoStep3Model>() {
    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: SignUpInfoStep3Model
        get() = ViewModelProviders.of(this, factory).get(SignUpInfoStep3Model::class.java)

    override val layoutId: Int
        get() = R.layout.fragment_sign_up_info_step3

    var adapterLanguages: ArrayAdapter<String>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapterLanguages =
            ArrayAdapter(context, android.R.layout.simple_list_item_1, arrayListOf<String>())
        viewDataBinding?.etBankName?.setAdapter(adapterLanguages)
        viewDataBinding?.etBankName?.threshold = 1

        viewModel.getBanks()
        viewModel.bankLiveData.observe(this, Observer<List<Bank>> { banks ->
            adapterLanguages?.clear()
            adapterLanguages?.addAll(banks.map { bank -> bank.name })
            adapterLanguages?.notifyDataSetChanged()
        })
    }

    override fun validate(): Boolean {
        return validator.validate()
    }
}
