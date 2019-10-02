package com.tebet.mojual.view.historysearch

import android.os.Bundle
import android.view.View
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import co.common.view.dialog.DateDialog
import co.common.view.dialog.SingleChoiceDialog
import com.tebet.mojual.R
import com.tebet.mojual.data.models.enumeration.OrderStatus
import com.tebet.mojual.data.models.enumeration.SortBy
import com.tebet.mojual.data.models.enumeration.SortType
import com.tebet.mojual.data.models.request.SearchOrderRequest
import com.tebet.mojual.databinding.FragmentHistorySearchBinding
import com.tebet.mojual.view.base.BaseFragment
import com.tebet.mojual.view.home.Home
import java.util.*


class HistorySearchFragment : BaseFragment<FragmentHistorySearchBinding, HistorySearchViewModel>(),
    HistorySearchNavigator {
    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: HistorySearchViewModel
        get() = ViewModelProviders.of(this, factory).get(HistorySearchViewModel::class.java)

    override val layoutId: Int
        get() = R.layout.fragment_history_search

    var searchRequest: SearchOrderRequest? = null
    var fromDateDialog: DateDialog? = null
    var toDateDialog: DateDialog? = null
    var statusChoiceDialog: SingleChoiceDialog<OrderStatus>? = null
    var sortByDialog: SingleChoiceDialog<SortBy>? = null
    var sortTypeDialog: SingleChoiceDialog<SortType>? = null

    companion object {
        fun newInstance(dataResponse: SearchOrderRequest): Fragment {
            val fragment = HistorySearchFragment()
            fragment.searchRequest = dataResponse
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator = this
        viewModel.loadData(false)
        searchRequest?.let {
            viewModel.searchRequest.set(it)
            viewModel.searchRequest.get()?.selectedSortBy = SortBy.ORDER_ID
        }
    }

    override fun openHistoryScreen(searchOrderRequest: SearchOrderRequest) {
        (activity as Home).viewModel.onHistoryClick(searchOrderRequest)
    }

    override fun openFromDatePicker() {
        if (fromDateDialog == null) {
            fromDateDialog = DateDialog()
            val next7Day = Calendar.getInstance()
            next7Day.add(Calendar.DAY_OF_MONTH, 8)
            fromDateDialog?.setMaxDate(next7Day)
            fromDateDialog?.setOnDateSetListener { view, year, month, dayOfMonth ->
                val cal = Calendar.getInstance()
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND, 0)
                viewModel.searchRequest.get()?.fromPlanDate = cal.timeInMillis
            }
        }
        activity?.let {
            fromDateDialog?.show(it.supportFragmentManager, "")
        }
    }

    override fun openToDatePicker() {
        if (toDateDialog == null) {
            toDateDialog = DateDialog()
            val next7Day = Calendar.getInstance()
            next7Day.add(Calendar.DAY_OF_MONTH, 8)
            toDateDialog?.setMaxDate(next7Day)
            toDateDialog?.setOnDateSetListener { view, year, month, dayOfMonth ->
                val cal = Calendar.getInstance()
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND, 0)
                viewModel.searchRequest.get()?.toPlanDate = cal.timeInMillis
            }
        }
        activity?.let {
            toDateDialog?.show(it.supportFragmentManager, "")
        }
    }

    override fun openOrderStatusPicker() {
        if (statusChoiceDialog == null) {
            statusChoiceDialog = StatusChoiceDialog().setCallback(object :
                SingleChoiceDialog.SingleChoiceDialogCallback<OrderStatus> {
                override fun onCancel() {
                }

                override fun onOk(selectedItem: OrderStatus?) {
                    viewModel.searchRequest.get()?.selectedStatus = selectedItem
                }
            })
        }
        statusChoiceDialog?.show(fragmentManager, "")
    }

    override fun openOrderByPicker() {
        if (sortByDialog == null) {
            sortByDialog = SortByDialog().setCallback(object :
                SingleChoiceDialog.SingleChoiceDialogCallback<SortBy> {
                override fun onCancel() {
                }

                override fun onOk(selectedItem: SortBy?) {
                    viewModel.searchRequest.get()?.selectedSortBy = selectedItem
                    viewModel.searchRequest.get()?.orders?.clear()
                    viewModel.searchRequest.get()?.selectedSortBy?.value?.let { it1 ->
                        viewModel.searchRequest.get()?.selectedSortType?.name?.let { it2 ->
                            viewModel.searchRequest.get()?.orders?.put(it1, it2)
                        }
                    }
                }
            })
        }
        sortByDialog?.show(fragmentManager, "")
    }

    override fun openOrderTypePicker() {
        if (sortTypeDialog == null) {
            sortTypeDialog = SortTypeDialog().setCallback(object :
                SingleChoiceDialog.SingleChoiceDialogCallback<SortType> {
                override fun onCancel() {
                }

                override fun onOk(selectedItem: SortType?) {
                    viewModel.searchRequest.get()?.selectedSortType = selectedItem!!
                    viewModel.searchRequest.get()?.orders?.let {
                        if (it.size > 0) {
                            it[it.keys.toTypedArray()[0]] = selectedItem.name
                            viewModel.searchRequest.get()?.orders = it
                        }
                    }
                }
            })
        }
        sortTypeDialog?.show(fragmentManager, "")
    }
}