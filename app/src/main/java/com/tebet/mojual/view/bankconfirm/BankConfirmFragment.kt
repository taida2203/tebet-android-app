package com.tebet.mojual.view.bankconfirm

import android.app.Activity
import android.content.Intent
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
import com.tebet.mojual.view.bankupdate.BankUpdate
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
        viewModel.order = order?.let { OrderDetail(it) }
        viewModel.selectedItems = selectedItems
        viewModel.loadData()
    }

    override fun openOrderDetailScreen(order: OrderDetail) {
        (activity as Home).viewModel.onOrderDetailClick(Order(order))
    }

    override fun openBankUpdateScreen() {
        startActivityForResult(Intent(context, BankUpdate::class.java), 500)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 500 && resultCode == Activity.RESULT_OK) {
            viewModel.loadData(true)
        }
    }
}
