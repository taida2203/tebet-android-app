package com.tebet.mojual.view.sale

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import br.com.ilhasoft.support.validation.Validator
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.data.models.Price
import com.tebet.mojual.databinding.FragmentSaleBinding
import com.tebet.mojual.view.base.BaseFragment
import com.tebet.mojual.view.home.Home
import com.tebet.mojual.view.selectfuturedate.SelectFutureDate
import com.tebet.mojual.view.selectquantity.SelectQuantity

open class SaleFragment : BaseFragment<FragmentSaleBinding, SaleViewModel>(), SaleNavigator {
    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: SaleViewModel
        get() = ViewModelProviders.of(this, factory).get(SaleViewModel::class.java)

    override val layoutId: Int
        get() = R.layout.fragment_sale

    private lateinit var validator: Validator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator = this
        validator = Validator(viewDataBinding)
        validator.enableFormValidationMode()
        viewModel.loadData()
    }

    override fun openSaleScreen(dataResponse: Order) {
        (activity as Home).viewModel.onSubmitOrderClick(dataResponse)
    }

    override fun showQuantityScreen() {
        startActivityForResult(Intent(context, SelectQuantity::class.java), 500)
    }

    override fun showDateScreen() {
        startActivityForResult(Intent(context, SelectFutureDate::class.java), 600)
    }

    override fun validate(): Boolean {
        return validator.validate()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                500 -> viewModel.selectedQuantity.value = data?.getStringExtra("QUANTITY")?.toInt()
                600 -> viewModel.selectedFutureDate.value = data?.getSerializableExtra("FUTURE_DATE") as Price
            }
        }
    }
}
