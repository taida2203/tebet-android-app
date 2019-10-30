package com.tebet.mojual.view.history

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProviders
import br.com.ilhasoft.support.validation.Validator
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import co.common.view.dialog.SingleChoiceDialog
import com.tebet.mojual.R
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.data.models.enumeration.OrderStatus
import com.tebet.mojual.data.models.request.SearchOrderRequest
import com.tebet.mojual.databinding.FragmentHistoryBinding
import com.tebet.mojual.view.base.BaseFragment
import com.tebet.mojual.view.historysearch.StatusChoiceDialog
import com.tebet.mojual.view.home.Home
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment : BaseFragment<FragmentHistoryBinding, HistoryViewModel>(), HistoryNavigator {

    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: HistoryViewModel
        get() = ViewModelProviders.of(this, factory).get(HistoryViewModel::class.java)

    override val layoutId: Int
        get() = R.layout.fragment_history

    private lateinit var validator: Validator

    var searchRequest: SearchOrderRequest? = null
    var statusChoiceDialog: SingleChoiceDialog<OrderStatus>? = null

    companion object {
        fun newInstance(dataResponse: SearchOrderRequest?): Fragment {
            val fragment = HistoryFragment()
            fragment.searchRequest = dataResponse
            return fragment
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator = this
        validator = Validator(viewDataBinding)
        filter_edit.showSoftInputOnFocus = false
        validator.enableFormValidationMode()
        searchRequest?.let {
            viewModel.searchRequest.set(it)
        }
        viewModel.loadData(false)
    }

    override fun itemSelected(item: Order) {
        (activity as Home).viewModel.onOrderDetailClick(item)
    }

    override fun openOrderStatusPicker() {
        if (statusChoiceDialog == null) {
            statusChoiceDialog = StatusChoiceDialog().setCallback(object :
                SingleChoiceDialog.SingleChoiceDialogCallback<OrderStatus> {
                override fun onCancel() {
                }

                override fun onOk(selectedItem: OrderStatus?) {
                    viewModel.searchRequest.set(SearchOrderRequest(status = if (selectedItem != OrderStatus.ALL) selectedItem?.name else null))
                    viewModel.loadData(true)
                }
            })
        }
        fragmentManager?.let { statusChoiceDialog?.show(it, "") }
    }

    override fun onResume() {

        super.onResume()
        hideKeyboard()
    }

    override fun validate(): Boolean {
        return validator.validate()
    }
}
