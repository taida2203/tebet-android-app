package com.tebet.mojual.view.saledetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.databinding.FragmentSaleDetailBinding
import com.tebet.mojual.view.base.BaseFragment
import com.tebet.mojual.view.home.HomeActivity

class SaleDetailFragment() : BaseFragment<FragmentSaleDetailBinding, SaleDetailViewModel>(), SaleDetailNavigator {
    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: SaleDetailViewModel
        get() = ViewModelProviders.of(this, factory).get(SaleDetailViewModel::class.java)

    override val layoutId: Int
        get() = R.layout.fragment_sale_detail

    var order: Order? = null

    companion object {
        fun newInstance(dataResponse: Order): Fragment {
            val fragment = SaleDetailFragment()
            fragment.order = dataResponse
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.order.set(order)
    }

    override fun openSaleScreen() {
        (activity as HomeActivity).viewModel.onSellClick()
    }
}
