package com.tebet.mojual.view.qualitycontainer

import android.app.Activity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import br.com.ilhasoft.support.validation.Validator
import co.common.view.dialog.RoundedDialog
import co.common.view.dialog.RoundedOkDialog
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.databinding.ActivityQualityAddContainerBinding
import com.tebet.mojual.databinding.ItemHomeIconBinding
import com.tebet.mojual.view.base.BaseActivity

class QualityAddContainer : BaseActivity<ActivityQualityAddContainerBinding, QualityAddContainerViewModel>(),
    QualityAddContainerNavigator {
    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: QualityAddContainerViewModel
        get() = ViewModelProviders.of(this, factory).get(QualityAddContainerViewModel::class.java)

    override val contentLayoutId: Int
        get() = R.layout.activity_quality_add_container

    private lateinit var validator: Validator
    private var topRightViewBinding: ItemHomeIconBinding? = null

    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
        viewModel.navigator = this
        enableBackButton = true
        validator = Validator(viewDataBinding)
        validator.enableFormValidationMode()
        title = getString(R.string.quality_add_container_title)
        topRightViewBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_home_icon, baseBinding.topRightHolder, true)
        intent.getSerializableExtra("EXTRA_ORDER")?.let {
            viewModel.order.set(it as Order)
            title = String.format(getString(R.string.check_quality_add_container_order), viewModel.order.get()?.orderCode)
        }
        viewModel.loadData()
    }

    override fun openConfirmScreen(dataResponse: Order) {
        intent.putExtra("EXTRA_ORDER", dataResponse)
        setResult(Activity.RESULT_OK, intent)
        RoundedOkDialog(getString(R.string.check_quality_add_container_turn_off_iot)).setRoundedDialogCallback(
            object : RoundedDialog.RoundedDialogCallback {
                override fun onFirstButtonClicked(selectedValue: Any?) {
                    finish()
                }

                override fun onSecondButtonClicked(selectedValue: Any?) {
                }
            }).show(supportFragmentManager, "")
    }

    override fun dataValid(): Boolean {
        hideKeyboard()
        return validator.validate()
    }
}
