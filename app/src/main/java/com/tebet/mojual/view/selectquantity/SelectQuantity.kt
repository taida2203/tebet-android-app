package com.tebet.mojual.view.selectquantity

import android.app.Activity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.library.baseAdapters.BR
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

    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
        viewModel.navigator = this
        viewModel.loadData()
    }

    override fun itemSelected(item: String) {
        intent.putExtra("QUANTITY", item)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
//    override fun showOrderCompleteScreen() {
//        enableBackButton = true
//        openFragment(SaleDetailFragment(), R.id.contentHolder)
//    }
}
