package com.tebet.mojual.view.help

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProviders
import com.tebet.mojual.R
import com.tebet.mojual.databinding.ActivityQualityHelpBinding
import com.tebet.mojual.databinding.ItemHomeIconBinding
import com.tebet.mojual.view.base.BaseActivity

open class QualityHelp : BaseActivity<ActivityQualityHelpBinding, QualityHelpViewModel>(),
    QualityHelpNavigator {
    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: QualityHelpViewModel
        get() = ViewModelProviders.of(this, factory).get(QualityHelpViewModel::class.java)

    override val contentLayoutId: Int
        get() = R.layout.activity_quality_help

    private var topRightViewBinding: ItemHomeIconBinding? = null

    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
        viewModel.navigator = this
        title = getString(R.string.quality_help_title)
        var url = "https://mo-jual.com/manual-setup"
        if (intent.hasExtra("EXTRA_ORDER")) url = intent.getStringExtra("EXTRA_ORDER")
        topRightViewBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_home_icon, baseBinding.topRightHolder, true)
        viewDataBinding?.webView?.loadUrl(url)
    }

    override fun openHomeScreen() {
        finish()
    }
}
