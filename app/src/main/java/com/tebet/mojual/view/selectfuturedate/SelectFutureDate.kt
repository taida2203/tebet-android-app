package com.tebet.mojual.view.selectfuturedate

import android.app.Activity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.data.models.Price
import com.tebet.mojual.databinding.ActivitySelectFutureDateBinding
import com.tebet.mojual.view.base.BaseActivity

class SelectFutureDate : BaseActivity<ActivitySelectFutureDateBinding, SelectFutureDateViewModel>(),
    SelectFutureDateNavigator {
    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: SelectFutureDateViewModel
        get() = ViewModelProviders.of(this, factory).get(SelectFutureDateViewModel::class.java)

    override val contentLayoutId: Int
        get() = R.layout.activity_select_future_date

    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
        viewModel.navigator = this
        viewModel.loadData()
    }

    override fun itemSelected(item: Price) {
        intent.putExtra("FUTURE_DATE", item)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
//    override fun showOrderDetailScreen() {
//        enableBackButton = true
//        openFragment(SaleDetailFragment(), R.id.contentHolder)
//    }
}
