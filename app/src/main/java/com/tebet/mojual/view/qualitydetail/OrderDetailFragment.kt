package com.tebet.mojual.view.qualitydetail

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import br.com.ilhasoft.support.validation.Validator
import co.common.view.dialog.RoundedDialog
import co.common.view.dialog.RoundedDialogButton
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.data.models.OrderContainer
import com.tebet.mojual.data.models.OrderDetail
import com.tebet.mojual.databinding.FragmentOrderDetailBinding
import com.tebet.mojual.view.base.BaseFragment
import com.tebet.mojual.view.home.Home

class OrderDetailFragment : BaseFragment<FragmentOrderDetailBinding, OrderDetailViewModel>(),
    OrderDetailNavigator {



    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: OrderDetailViewModel
        get() = ViewModelProviders.of(this, factory).get(OrderDetailViewModel::class.java)

    override val layoutId: Int
        get() = R.layout.fragment_order_detail

    private lateinit var validator: Validator

    var order: Order? = null

    companion object {
        fun newInstance(dataResponse: Order): Fragment {
            val fragment = OrderDetailFragment()
            fragment.order = dataResponse
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator = this
        validator = Validator(viewDataBinding)
        validator.enableFormValidationMode()
        viewModel.order.set(order?.let { OrderDetail(it) })
        viewModel.loadData()
    }

    override fun itemSelected(item: OrderContainer) {
    }

    override fun validate(): Boolean {
        return validator.validate()
    }

    override fun openHomeScreen() {
        (activity as Home).viewModel.onHomeClick()
    }

    override fun openRejectReasonScreen(order: OrderDetail, selectedItems: List<OrderContainer>) {
        (activity as Home).viewModel.onReasonClick(order, selectedItems)
    }
    override fun showConfirmDialog(selectedItems: List<OrderContainer>) {
        activity?.supportFragmentManager?.let {
            RoundedDialog("Are you sure to sell your selected container?")
                .addFirstButton(RoundedDialogButton("NO"))
                .addSecondButton(RoundedDialogButton("YES"))
                .setRoundedDialogCallback(
                object : RoundedDialog.RoundedDialogCallback {
                    override fun onFirstButtonClicked(selectedValue: Any?) {
                    }

                    override fun onSecondButtonClicked(selectedValue: Any?) {
                        viewModel.submitOrder(selectedItems)
                    }
                }).show(it, "")
        }
    }
}
