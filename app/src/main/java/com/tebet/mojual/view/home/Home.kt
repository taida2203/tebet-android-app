package com.tebet.mojual.view.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.library.baseAdapters.BR
import com.tebet.mojual.R
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.data.models.OrderContainer
import com.tebet.mojual.data.models.OrderDetail
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.models.request.SearchOrderRequest
import com.tebet.mojual.databinding.ActivityHomeBinding
import com.tebet.mojual.databinding.ItemHomeAvatarBinding
import com.tebet.mojual.databinding.ItemHomeIconBinding
import com.tebet.mojual.view.bankconfirm.BankConfirmFragment
import com.tebet.mojual.view.base.BaseActivity
import com.tebet.mojual.view.history.HistoryFragment
import com.tebet.mojual.view.home.content.HomeFragment
import com.tebet.mojual.view.historysearch.HistorySearchFragment
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
import com.tebet.mojual.view.message.MessageFragment


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
        when {
            !openFromNotification(bundleToMap(intent.extras)) -> showHomeScreen()
        }
        viewModel.loadData()
        topLeftViewBinding?.avatar?.setOnClickListener { showProfileScreen() }
        viewModel.profileLiveData.observe(
            this,
            Observer<UserProfile> { topLeftViewBinding?.userProfile = it })
    }

    private fun bundleToMap(extras: Bundle?): Map<String, String?> {
        val map = HashMap<String, String?>()
        val ks = extras?.keySet()
        val iterator = ks?.iterator()
        iterator?.let {
            while (it.hasNext()) {
                val key = it.next()
                map[key] = extras.getString(key)
            }
        }
        return map
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
        if (currentFragment() is SaleDetailFragment) {
            supportFragmentManager.popBackStack()
        }
        openFragmentSlideRight(SaleFragment(), R.id.contentHolder, SaleFragment::class.java.simpleName)
    }

    override fun showHomeScreen() = openFragmentSlideRight(HomeFragment(), R.id.contentHolder, HomeFragment::class.java.simpleName)

    override fun showHistoryScreen() = openFragmentSlideRight(HistoryFragment(), R.id.contentHolder, HistoryFragment::class.java.simpleName)

    override fun showHistorySearchScreen(searchOrderRequest: SearchOrderRequest) = openFragmentSlideRight(HistorySearchFragment(), R.id.contentHolder, HistorySearchFragment::class.java.simpleName)


    override fun showInboxScreen() = openFragmentSlideRight(MessageFragment(), R.id.contentHolder, MessageFragment::class.java.simpleName)

    override fun showProfileScreen() = openFragmentSlideRight(ProfileFragment(), R.id.contentHolder, ProfileFragment::class.java.simpleName)

    override fun showBankConfirmScreen(order: OrderDetail, selectedItems: List<OrderContainer>)  = openFragmentSlideRight(BankConfirmFragment.newInstance(order, selectedItems), R.id.contentHolder, BankConfirmFragment::class.java.simpleName)

    override fun showOrderCompleteScreen(dataResponse: Order) {
        supportFragmentManager.popBackStack()
        openFragmentSlideRight(SaleDetailFragment.newInstance(dataResponse), R.id.contentHolder, SaleDetailFragment::class.java.simpleName, dataResponse.orderCode)
    }

    override fun showOrderDetailScreen(dataResponse: Order) {
        if (currentFragment() !is OrderDetailFragment) {
            if (currentFragment() is OrderRejectFragment || currentFragment() is BankConfirmFragment) {
                supportFragmentManager.popBackStack()
            } else {
                openFragmentSlideRight(OrderDetailFragment.newInstance(dataResponse), R.id.contentHolder, OrderDetailFragment::class.java.simpleName, dataResponse.orderCode)
            }
        } else {
            (currentFragment() as OrderDetailFragment).viewModel.order.set(OrderDetail(dataResponse))
            (currentFragment() as OrderDetailFragment).viewModel.loadData()
        }
    }

    override fun showCheckQualityScreen() = openFragmentSlideRight(QualityFragment(), R.id.contentHolder, QualityFragment::class.java.simpleName)

    override fun showRejectReasonScreen(order: OrderDetail, selectedItems: List<OrderContainer>) = openFragmentSlideRight(OrderRejectFragment.newInstance(order, selectedItems), R.id.contentHolder, OrderRejectFragment::class.java.simpleName, order.orderCode)

    override fun showAddContainerScreen(dataResponse: Order) {
        val mIntent = Intent(this, QualityAddContainer::class.java)
        mIntent.putExtra("EXTRA_ORDER", dataResponse)
        startActivityForResult(mIntent, 500)
    }


    private fun openFragmentSlideRight(fragment: Fragment, placeHolder: Int, backStackTag: String?, title: String?) {
        this.openFragmentSlideRight(fragment, placeHolder, backStackTag)
        updateTitleAction(fragment, title)
    }

    override fun openFragmentSlideRight(fragment: Fragment, placeHolder: Int, backStackTag: String?) {
        currentFragment()?.let {
            if (it::class == fragment::class) return
        }
        updateTitleAction(fragment)
        if (fragment is HomeFragment) if (currentFragment() !is HomeFragment) supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        super.openFragmentSlideRight(fragment, placeHolder, backStackTag)
    }

    override fun onBackPressed() {
        if (currentFragment() == null || currentFragment() is HomeFragment) finish()
        super.onBackPressed()
        updateTitleAction(currentFragment())
    }

    private fun currentFragment(): Fragment? {
        supportFragmentManager?.let {
            try {
                return it.findFragmentByTag(it.getBackStackEntryAt(it.backStackEntryCount - 1).name)
            } catch (e: Exception) {
            }
        }
        return null
    }

    private fun updateTitleAction(fragment: Fragment?, titleString: String? = null) {
        baseBinding.viewModel?.enableTopLogo?.set(false)
        enableBackButton = true
        when (fragment) {
            is HomeFragment -> {
                enableBackButton = false
                baseBinding.viewModel?.enableTopLogo?.set(true)
                viewModel.selectedTab.set(HomeViewModel.ScreenTab.Home)
            }
            is HistoryFragment -> {
                viewModel.selectedTab.set(HomeViewModel.ScreenTab.History)
                title = getString(R.string.history_title)
            }
            is MessageFragment -> {
                viewModel.selectedTab.set(HomeViewModel.ScreenTab.Message)
                title = getString(R.string.message_title)
            }
            is ProfileFragment -> title = viewModel.profileLiveData.value?.fullName
            is OrderRejectFragment -> title = getString(R.string.order_reject_title)
            is SaleFragment -> title = getString(R.string.home_menu_sell_now)
            is SaleDetailFragment -> title = String.format(getString(R.string.check_quality_add_container_order), titleString ?: fragment.order?.orderCode)
            is OrderDetailFragment -> title = getString(R.string.order_detail_title)
            is QualityFragment -> title = getString(R.string.home_menu_check_quality)
            is HistorySearchFragment -> title = getString(R.string.history_search_title)
            is BankConfirmFragment -> title = getString(R.string.bank_confirm_title)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 500) {
            if (resultCode == Activity.RESULT_OK) {
                data?.getSerializableExtra("EXTRA_ORDER")?.let {
                    if (currentFragment() is QualityFragment) {
                        (currentFragment() as QualityFragment).viewModel.items.remove(it)
                    } else if (currentFragment() is SaleFragment) {
                        supportFragmentManager.popBackStack()
                    }
                    showOrderDetailScreen(it as Order)
                }
            }
        }
    }
}
