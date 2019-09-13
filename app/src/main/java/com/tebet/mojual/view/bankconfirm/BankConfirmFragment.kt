package com.tebet.mojual.view.bankconfirm

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import br.com.ilhasoft.support.validation.Validator
import androidx.databinding.library.baseAdapters.BR
import com.tebet.mojual.R
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.data.models.OrderContainer
import com.tebet.mojual.data.models.OrderDetail
import com.tebet.mojual.databinding.FragmentBankConfirmBinding
import com.tebet.mojual.databinding.FragmentOrderRejectBinding
import com.tebet.mojual.view.base.BaseFragment
import com.tebet.mojual.view.home.Home

class BankConfirmFragment : BaseFragment<FragmentBankConfirmBinding, BankConfirmViewModel>(),
    BankConfirmNavigator {


    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: BankConfirmViewModel
        get() = ViewModelProviders.of(this, factory).get(BankConfirmViewModel::class.java)

    override val layoutId: Int
        get() = R.layout.fragment_bank_confirm

    private lateinit var validator: Validator

    var order: OrderDetail? = null
    var selectedItems: List<OrderContainer>? = null

    companion object {
        fun newInstance(
            dataResponse: OrderDetail,
            selectedItems: List<OrderContainer>
        ): Fragment {
            val fragment = BankConfirmFragment()
            fragment.order = dataResponse
            fragment.selectedItems = selectedItems
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator = this
        validator = Validator(viewDataBinding)
        validator.enableFormValidationMode()
        viewModel.order.set(order?.let { OrderDetail(it) })
        selectedItems?.let { viewModel.items.addAll(it) }
    }

    override fun validate(): Boolean {
        return validator.validate()
    }

    override fun openOrderDetailScreen(order: OrderDetail) {
        (activity as Home).viewModel.onOrderDetailClick(Order(order))
    }
}
