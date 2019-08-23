package com.tebet.mojual.view.qualitydetail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import br.com.ilhasoft.support.validation.Validator
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.data.models.IOrder
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.data.models.OrderContainer
import com.tebet.mojual.data.models.OrderDetail
import com.tebet.mojual.databinding.FragmentOrderDetailBinding
import com.tebet.mojual.view.base.BaseFragment
import com.tebet.mojual.view.home.Home
import com.tebet.mojual.view.qualitycontainer.QualityAddContainer

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

    override fun openSellScreen() {
        (activity as Home).viewModel.onCheckQualityNowClick()
    }

    override fun openAddContainerScreen(selectedItem: IOrder) {
        val mIntent = Intent(context, QualityAddContainer::class.java)
        mIntent.putExtra("EXTRA_ORDER", selectedItem as Order)
        startActivityForResult(mIntent, 500)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            500 -> {
                if (resultCode == Activity.RESULT_OK) {

                }
            }
        }
    }
}
