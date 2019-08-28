package com.tebet.mojual.view.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.data.models.OrderContainer
import com.tebet.mojual.data.models.OrderDetail
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.databinding.ActivityHomeBinding
import com.tebet.mojual.databinding.ItemHomeAvatarBinding
import com.tebet.mojual.databinding.ItemHomeIconBinding
import com.tebet.mojual.view.base.BaseActivity
import com.tebet.mojual.view.history.HistoryFragment
import com.tebet.mojual.view.home.content.HomeFragment
import com.tebet.mojual.view.profile.ProfileFragment
import com.tebet.mojual.view.qualitycheck.QualityFragment
import com.tebet.mojual.view.qualitycontainer.QualityAddContainer
import com.tebet.mojual.view.qualitydetail.OrderDetailFragment
import com.tebet.mojual.view.qualityreject.OrderRejectFragment
import com.tebet.mojual.view.sale.SaleFragment
import com.tebet.mojual.view.saledetail.SaleDetailFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class Home : BaseActivity<ActivityHomeBinding, HomeViewModel>(), HasSupportFragmentInjector,
    HomeNavigator {
    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> =
        fragmentDispatchingAndroidInjector

    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: HomeViewModel
        get() = ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)

    override val contentLayoutId: Int
        get() = R.layout.activity_home

    private var currentFragment: Fragment? = null

    private var topLeftViewBinding: ItemHomeAvatarBinding? = null
    private var topRightViewBinding: ItemHomeIconBinding? = null

    override var enableBackButton: Boolean = super.enableBackButton
        set(isEnable) {
            field = isEnable
            super.enableBackButton = isEnable
        }

    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
        viewModel.navigator = this
        topLeftViewBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_home_avatar,
            baseBinding.topLeftHolder,
            true
        )
        topRightViewBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_home_icon,
            baseBinding.topRightHolder,
            true
        )
        showHomeScreen()
        viewModel.loadData()
        topLeftViewBinding?.avatar?.setOnClickListener { showProfileScreen() }
        viewModel.profileLiveData.observe(
            this,
            Observer<UserProfile> { topLeftViewBinding?.userProfile = it })
    }

    override fun showCheckQualityScreen() {
        enableBackButton = true
        baseBinding.viewModel?.enableTopLogo?.set(false)
        title = getString(R.string.home_menu_check_quality)
        openFragmentSlideRight(QualityFragment(), R.id.contentHolder)
    }

    override fun showBorrowScreen() {
        enableBackButton = true
        baseBinding.viewModel?.enableTopLogo?.set(false)
        title = getString(R.string.home_menu_borrow)
    }

    override fun showTipsScreen() {
        enableBackButton = true
        baseBinding.viewModel?.enableTopLogo?.set(false)
        title = getString(R.string.home_menu_tips)
    }

    override fun showSellScreen() {
        enableBackButton = true
        baseBinding.viewModel?.enableTopLogo?.set(false)
        title = getString(R.string.home_menu_sell_now)
        openFragmentSlideRight(SaleFragment(), R.id.contentHolder)
    }

    override fun showHomeScreen() {
        enableBackButton = false
        baseBinding.viewModel?.enableTopLogo?.set(true)
        openFragmentSlideRight(HomeFragment(), R.id.contentHolder)
    }

    override fun showHistoryScreen() {
        enableBackButton = false
        baseBinding.viewModel?.enableTopLogo?.set(true)
        openFragmentSlideRight(HistoryFragment(), R.id.contentHolder)
    }


    override fun showProfileScreen() {
        enableBackButton = true
        baseBinding.viewModel?.enableTopLogo?.set(false)
        title = viewModel.profileLiveData.value?.fullName
        openFragmentSlideRight(ProfileFragment(), R.id.contentHolder)
    }

    override fun showOrderCompleteScreen(dataResponse: Order) {
        enableBackButton = true
        baseBinding.viewModel?.enableTopLogo?.set(false)
        title = String.format(
            getString(R.string.check_quality_add_container_order),
            dataResponse.orderCode
        )
        openFragmentSlideRight(SaleDetailFragment.newInstance(dataResponse), R.id.contentHolder)
    }

    override fun showOrderDetailScreen(dataResponse: Order) {
        enableBackButton = true
        baseBinding.viewModel?.enableTopLogo?.set(false)
        title = String.format(
            getString(R.string.check_quality_add_container_order),
            dataResponse.orderCode
        )
        openFragmentSlideRight(OrderDetailFragment.newInstance(dataResponse), R.id.contentHolder)
    }

    override fun showAddContainerScreen(dataResponse: Order) {
        val mIntent = Intent(this, QualityAddContainer::class.java)
        mIntent.putExtra("EXTRA_ORDER", dataResponse)
        startActivityForResult(mIntent, 500)
        showCheckQualityScreen()
    }

    override fun showRejectReasonScreen(order: OrderDetail, selectedItems: List<OrderContainer>) {
        enableBackButton = true
        baseBinding.viewModel?.enableTopLogo?.set(false)
        title = getString(R.string.home_reject_title)
        openFragmentSlideRight(OrderRejectFragment.newInstance(order, selectedItems), R.id.contentHolder)
    }

    override fun openFragmentSlideRight(fragment: Fragment, placeHolder: Int, backStackTag: String?) {
        currentFragment = fragment
        super.openFragmentSlideRight(fragment, placeHolder, backStackTag)
    }

    override fun onBackPressed() {
        if (currentFragment != null && currentFragment !is HomeFragment) {
            showHomeScreen()
            return
        }
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 500) {
            if (resultCode == Activity.RESULT_OK) {
                data?.getSerializableExtra("EXTRA_ORDER")?.let {
                    showOrderDetailScreen(it as Order)
                }
            }
        }
    }
}
