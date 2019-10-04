package com.tebet.mojual.view.signup.step3

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.library.baseAdapters.BR
import com.tebet.mojual.R
import com.tebet.mojual.data.models.Bank
import com.tebet.mojual.data.models.Region
import com.tebet.mojual.databinding.FragmentSignUpInfoStep3Binding
import com.tebet.mojual.view.signup.SignUpInfo
import com.tebet.mojual.view.signup.step.SignUpInfoStep

class SignUpInfoStep3 : SignUpInfoStep<FragmentSignUpInfoStep3Binding, SignUpInfoStep3Model>(), SignUpInfoStep3Navigator {

    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: SignUpInfoStep3Model
        get() = ViewModelProviders.of(this, factory).get(SignUpInfoStep3Model::class.java)

    override val layoutId: Int
        get() = R.layout.fragment_sign_up_info_step3

    private var bankAdapter: ArrayAdapter<String>? = null
    private var regionAdapter: ArrayAdapter<String>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator = this
        bankAdapter =
            ArrayAdapter(context, android.R.layout.simple_list_item_1, arrayListOf<String>())
        viewDataBinding?.etBankName?.setAdapter(bankAdapter)
        viewDataBinding?.etBankName?.threshold = 1
        viewDataBinding?.etBankName?.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val currentItemString = bankAdapter?.getItem(position)
                val selectedItem =
                    viewModel.bankNameLiveData.value?.firstOrNull { bank -> bank.name == currentItemString }

                viewModel.userProfile.get()?.bankName = selectedItem?.name
                viewModel.userProfile.get()?.bankCode = selectedItem?.code
            }
        viewDataBinding?.etBankName?.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateBankCode()
                validator.validate(view)
            }
        }
        regionAdapter =
            ArrayAdapter(context, android.R.layout.simple_list_item_1, arrayListOf<String>())
        viewDataBinding?.etRegionName?.setAdapter(regionAdapter)
        viewDataBinding?.etRegionName?.threshold = 1
        viewDataBinding?.etRegionName?.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val currentItemString = regionAdapter?.getItem(position)
                val selectedItem =
                    viewModel.bankRegionLiveData.value?.firstOrNull { region -> region.name == currentItemString }

                viewModel.userProfile.get()?.bankRegionName = selectedItem?.name
                viewModel.userProfile.get()?.bankRegionCode = selectedItem?.code
            }
        viewDataBinding?.etRegionName?.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                validateBankRegionCode()
                validator.validate(v)
            }
        }
        viewModel.loadData()
        viewModel.bankNameLiveData.observe(this, Observer<List<Bank>> { banks ->
            bankAdapter?.clear()
            bankAdapter?.addAll(banks.map { bank -> bank.name })
            bankAdapter?.notifyDataSetChanged()
        })
        viewModel.bankRegionLiveData.observe(this, Observer<List<Region>> { regions ->
            regionAdapter?.clear()
            regionAdapter?.addAll(regions.map { region -> region.name })
            regionAdapter?.notifyDataSetChanged()
        })
    }

    private fun validateBankCode() {
        if (TextUtils.isEmpty(viewModel.userProfile.get()?.bankCode)) viewModel.userProfile.get()?.bankName = null
    }

    private fun validateBankRegionCode() {
        if (TextUtils.isEmpty(viewModel.userProfile.get()?.bankRegionCode)) viewModel.userProfile.get()
            ?.bankRegionName = null
    }

    override fun validate(): Boolean {
        viewDataBinding?.etBankName?.clearFocus()
        viewDataBinding?.etRegionName?.clearFocus()
        return validator.validate()
    }

    override fun submit() {
        try {
            (activity as SignUpInfo).viewDataBinding?.btnNext?.performClick()
        } catch (e: Exception) {
        }
    }
}
