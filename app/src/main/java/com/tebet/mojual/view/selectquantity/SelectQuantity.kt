package com.tebet.mojual.view.selectquantity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.databinding.ActivitySelectQuantityBinding
import com.tebet.mojual.databinding.ItemHomeIconBinding
import com.tebet.mojual.view.base.BaseActivity

class SelectQuantity : BaseActivity<ActivitySelectQuantityBinding, SelectQuantityViewModel>(), SelectQuantityNavigator {
    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: SelectQuantityViewModel
        get() = ViewModelProviders.of(this, factory).get(SelectQuantityViewModel::class.java)

    override val contentLayoutId: Int
        get() = R.layout.activity_select_quantity

    private var topRightViewBinding: ItemHomeIconBinding? = null

    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
        viewModel.navigator = this
        topRightViewBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_home_icon, baseBinding.topRightHolder, true)
        baseBinding.viewModel?.enableTopLogo?.set(true)
        title = "Home"
        viewModel.loadData()
    }
//    override fun showOrderDetailScreen() {
//        enableBackButton = true
//        openFragment(SaleDetailFragment(), R.id.contentHolder)
//    }
}
