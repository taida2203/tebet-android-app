package com.tebet.mojual.view.qualitycheck

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.lifecycle.ViewModelProviders
import br.com.ilhasoft.support.validation.Validator
import androidx.databinding.library.baseAdapters.BR
import com.tebet.mojual.R
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.databinding.FragmentQualityBinding
import com.tebet.mojual.view.base.BaseFragment
import com.tebet.mojual.view.help.QualityHelp
import com.tebet.mojual.view.home.Home
import com.tebet.mojual.view.qualitycontainer.QualityAddContainer
import kotlinx.android.synthetic.main.fragment_quality.*

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
        val textConcat = getString(R.string.check_quality_tip1) + " <font color='#49802D'>" + getString(R.string.check_quality_tip2) + "</font>"
        tipText.text = Html.fromHtml(textConcat)
        validator = Validator(viewDataBinding)
        validator.enableFormValidationMode()
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
        (activity as Home).viewModel.onQualityCheckOrderSelected(selectedItem)
    }

    override fun openQualityHelpScreen() {
        startActivity(Intent(activity, QualityHelp::class.java))
    }
}
