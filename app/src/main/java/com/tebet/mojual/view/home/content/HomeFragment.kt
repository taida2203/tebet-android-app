package com.tebet.mojual.view.home.content

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.library.baseAdapters.BR
import co.common.view.dialog.RoundedDialog
import co.common.view.dialog.RoundedDialogButton
import com.tebet.mojual.R
import com.tebet.mojual.databinding.FragmentHomeBinding
import com.tebet.mojual.view.base.BaseFragment
import com.tebet.mojual.view.home.Home

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeContentViewModel>(), HomeContentNavigator {
    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: HomeContentViewModel
        get() = ViewModelProviders.of(this, factory).get(HomeContentViewModel::class.java)

    override val layoutId: Int
        get() = R.layout.fragment_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navigator = this
        viewModel.loadData()
    }

    override fun openSellScreen() {
        (activity as Home).viewModel.onSellClick()
    }

    override fun openQualityCheckScreen() {
        (activity as Home).viewModel.onQualityCheckClick()
    }

    override fun openTipsScreen() {
        (activity as Home).showTipScreen()
    }

    override fun showFeatureDisabled() {
        activity?.supportFragmentManager?.let {
            RoundedDialog(getString(R.string.general_error_feature_disabled))
                .addFirstButton(RoundedDialogButton(getString(R.string.sale_asset_empty_button)))
                .show(it, getString(R.string.general_error_feature_disabled))
        }
    }

}
