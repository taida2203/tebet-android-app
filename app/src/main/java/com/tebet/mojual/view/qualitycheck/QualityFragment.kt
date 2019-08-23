package com.tebet.mojual.view.qualitycheck

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import br.com.ilhasoft.support.validation.Validator
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.databinding.FragmentQualityBinding
import com.tebet.mojual.view.base.BaseFragment
import com.tebet.mojual.view.home.Home
import com.tebet.mojual.view.qualitycontainer.QualityAddContainer

class QualityFragment : BaseFragment<FragmentQualityBinding, QualityViewModel>(), QualityNavigator {
    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: QualityViewModel
        get() = ViewModelProviders.of(this, factory).get(QualityViewModel::class.java)

    override val layoutId: Int
        get() = R.layout.fragment_quality

    private lateinit var validator: Validator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator = this
        validator = Validator(viewDataBinding)
        validator.enableFormValidationMode()
        viewModel.loadData()
    }

    override fun itemSelected(item: Order) {
    }

    override fun validate(): Boolean {
        return validator.validate()
    }

    override fun openSellScreen() {
        (activity as Home).viewModel.onCheckQualityNowClick()
    }

    override fun openAddContainerScreen(selectedItem: Order) {
        (activity as Home).viewModel.onOrderDetailClick(selectedItem)
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
