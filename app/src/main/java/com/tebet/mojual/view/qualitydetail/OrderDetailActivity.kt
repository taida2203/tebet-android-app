package com.tebet.mojual.view.qualitydetail

import android.app.Activity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tebet.mojual.R
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.databinding.ActivityOrderDetailBinding
import com.tebet.mojual.databinding.ItemHomeIconBinding
import com.tebet.mojual.view.base.BaseActivity
import com.tebet.mojual.view.qualitycheck.QualityFragment
import com.tebet.mojual.view.sale.SaleFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class OrderDetailActivity :
    BaseActivity<ActivityOrderDetailBinding, OrderDetailActivityViewModel>(),
    OrderDetailActivityNavigator, HasSupportFragmentInjector {
    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: OrderDetailActivityViewModel
        get() = ViewModelProviders.of(this, factory).get(OrderDetailActivityViewModel::class.java)

    override val contentLayoutId: Int
        get() = R.layout.activity_order_detail

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    private var topRightViewBinding: ItemHomeIconBinding? = null

    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
        viewModel.navigator = this
        title = getString(R.string.order_detail_title)
        topRightViewBinding =
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.item_home_icon,
                baseBinding.topRightHolder,
                true
            )

        var currentFragment: OrderDetailFragment? = null
        intent?.getSerializableExtra("EXTRA_ORDER")?.let {
            currentFragment = OrderDetailFragment.newInstance(it as Order)
            openFragment(currentFragment!!, R.id.placeHolderChild)
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> =
        fragmentDispatchingAndroidInjector

    override fun openPreviousScreen() {
        setResult(Activity.RESULT_OK)
        finish()
    }

}
