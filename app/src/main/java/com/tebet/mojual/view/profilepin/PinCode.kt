package com.tebet.mojual.view.profilepin

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProviders
import com.tebet.mojual.R
import com.tebet.mojual.databinding.ActivityPinCodeBinding
import com.tebet.mojual.databinding.ActivityQualityHelpBinding
import com.tebet.mojual.databinding.ItemHomeIconBinding
import com.tebet.mojual.view.base.BaseActivity

open class PinCode : BaseActivity<ActivityPinCodeBinding, PinCodeViewModel>(),
    PinCodeNavigator {
    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: PinCodeViewModel
        get() = ViewModelProviders.of(this, factory).get(PinCodeViewModel::class.java)

    override val contentLayoutId: Int
        get() = R.layout.activity_pin_code

    private var topRightViewBinding: ItemHomeIconBinding? = null

    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
        viewModel.navigator = this
        title = getString(R.string.quality_help_title)
        topRightViewBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_home_icon, baseBinding.topRightHolder, true)
        viewDataBinding?.webView?.loadUrl("https://lotus.vn")
    }

    override fun openHomeScreen() {
        finish()
    }
}
