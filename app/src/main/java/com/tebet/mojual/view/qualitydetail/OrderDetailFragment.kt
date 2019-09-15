package com.tebet.mojual.view.qualitydetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import br.com.ilhasoft.support.validation.Validator
import co.common.view.dialog.RoundedDialog
import co.common.view.dialog.RoundedDialogButton
import androidx.databinding.library.baseAdapters.BR
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

    override fun openOrderDetailScreen(it: OrderDetail) {
        (activity as Home).viewModel.onOrderDetailClick(Order(it))
    }

    override fun openBankConfirmScreen(order: OrderDetail, selectedItems: List<OrderContainer>) {
        (activity as Home).viewModel.onBankConfirmClick(order, selectedItems)
    }

    override fun showRejectConfirm() {
        activity?.supportFragmentManager?.let {
            RoundedDialog(getString(R.string.order_detail_dialog_reject))
                .addFirstButton(RoundedDialogButton(getString(R.string.general_btn_no), R.drawable.rounded_bg_button_trans))
                .addSecondButton(RoundedDialogButton(getString(R.string.general_btn_yes)))
                .setRoundedDialogCallback(
                    object : RoundedDialog.RoundedDialogCallback {
                        override fun onFirstButtonClicked(selectedValue: Any?) {
                        }

                        override fun onSecondButtonClicked(selectedValue: Any?) {
                            viewModel.rejectOrder()
                        }
                    }).show(it, "")
        }
    }

    override fun openReasonScreen(order: OrderDetail, selectedItems: List<OrderContainer>) {
        (activity as Home).viewModel.onReasonClick(order, selectedItems)
    }

    override fun showConfirmDialog(selectedItems: List<OrderContainer>) {
        activity?.supportFragmentManager?.let {
            RoundedDialog(getString(R.string.order_detail_dialog_confirm))
                .addFirstButton(RoundedDialogButton(getString(R.string.general_btn_no), R.drawable.rounded_bg_button_trans))
                .addSecondButton(RoundedDialogButton(getString(R.string.general_btn_yes)))
                .setRoundedDialogCallback(
                object : RoundedDialog.RoundedDialogCallback {
                    override fun onFirstButtonClicked(selectedValue: Any?) {
                    }

                    override fun onSecondButtonClicked(selectedValue: Any?) {
                        viewModel.approveOrder(selectedItems)
                    }
                }).show(it, "")
        }
    }
}
