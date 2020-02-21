package com.tebet.mojual.view.sale

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProviders
import br.com.ilhasoft.support.validation.Validator
import co.common.view.dialog.RoundedDialog
import co.common.view.dialog.RoundedDialogButton
import com.tebet.mojual.R
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.data.models.Price
import com.tebet.mojual.databinding.FragmentSaleBinding
import com.tebet.mojual.view.base.BaseFragment
import com.tebet.mojual.view.home.Home
import com.tebet.mojual.view.selectfuturedate.SelectFutureDate
import com.tebet.mojual.view.selectquantity.SelectQuantity
import kotlinx.android.synthetic.main.fragment_sale.*

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
        snContainerType.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                clearContainerTypeError()
                viewModel.selectedQuantity.value = null
                viewModel.selectedItemType.set(position)
            }
        }
        validator = Validator(viewDataBinding)
        validator.enableFormValidationMode()
        viewModel.loadData()
    }

    override fun openSaleDetailScreen(dataResponse: Order) {
        if (viewModel.selectedFutureDate.value?.isToday == true) {
            (activity as Home).viewModel.onSubmitOrderNowClick(dataResponse)
        } else {
            (activity as Home).viewModel.onSubmitOrderClick(dataResponse)
        }
    }

    override fun showQuantityScreen() {
        viewModel.getSelectedContainerType()?.let {
            var intent = Intent(context, SelectQuantity::class.java)
            intent.putExtra("CONTAINER_TYPE", it.name)
            startActivityForResult(intent, 500)
        }
    }

    override fun showDateScreen() {
        viewModel.getSelectedContainerType()?.let {
            var intent = Intent(context, SelectFutureDate::class.java)
            intent.putExtra("CONTAINER_TYPE", it.name)
            startActivityForResult(intent, 600)
        }
    }

    override fun validate(): Boolean {
        val validateContainerType = viewModel.getSelectedContainerType() != null
        when {
            validateContainerType -> clearContainerTypeError()
            else -> showContainerTypeError()
        }
        return validator.validate() && validateContainerType
    }

    override fun showEmptyAsset() {
        activity?.supportFragmentManager?.let {
            RoundedDialog(getString(R.string.sale_asset_empty))
                .addFirstButton(RoundedDialogButton(getString(R.string.sale_asset_empty_button)))
                .show(it, getString(R.string.sale_asset_empty))
        }
    }

    override fun showContainerTypeError() {
        containerTypeValidate.error = getString(R.string.error_message_empty_validation)
    }

    fun clearContainerTypeError() {
        containerTypeValidate.error = null
        containerTypeValidate.invalidate()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                500 -> viewModel.selectedQuantity.value = data?.getStringExtra("QUANTITY")?.toInt()
                600 -> viewModel.selectedFutureDate.value =
                    data?.getSerializableExtra("FUTURE_DATE") as Price
            }
        }
    }
}
