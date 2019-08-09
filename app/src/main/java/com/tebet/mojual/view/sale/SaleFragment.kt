package com.tebet.mojual.view.sale

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.databinding.FragmentSaleBinding
import com.tebet.mojual.view.base.BaseFragment
import com.tebet.mojual.view.home.HomeActivity
import com.tebet.mojual.view.selectfuturedate.SelectFutureDate
import com.tebet.mojual.view.selectquantity.SelectQuantity

class SaleFragment : BaseFragment<FragmentSaleBinding, SaleViewModel>(), SaleNavigator {
    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: SaleViewModel
        get() = ViewModelProviders.of(this, factory).get(SaleViewModel::class.java)

    override val layoutId: Int
        get() = R.layout.fragment_sale

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navigator = this
    }

    override fun openSaleScreen() {
        (activity as HomeActivity).viewModel.onSubmitOrderClick()
    }

    override fun showQuantityScreen() {
        startActivityForResult(Intent(context, SelectQuantity::class.java), 500)
    }

    override fun showDateScreen() {
        startActivityForResult(Intent(context, SelectFutureDate::class.java), 600)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                500 -> {
                    Toast.makeText(context, data?.getStringExtra("QUANTITY"), Toast.LENGTH_SHORT).show()
                }
                600 -> {
                    Toast.makeText(context, data?.getStringExtra("FUTURE_DATE"), Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

}
