package com.tebet.mojual.view.home

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.data.models.Order
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
import com.tebet.mojual.view.sale.SaleFragment
import com.tebet.mojual.view.saledetail.SaleDetailFragment
import com.tebet.mojual.view.salenow.SaleNowFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject
import dagger.android.support.HasSupportFragmentInjector

class Home : BaseActivity<ActivityHomeBinding, HomeViewModel>(), HasSupportFragmentInjector, HomeNavigator {
    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector

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
        topLeftViewBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_home_avatar, baseBinding.topLeftHolder, true)
        topRightViewBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_home_icon, baseBinding.topRightHolder, true)
        showHomeScreen()
        viewModel.loadData()
        topLeftViewBinding?.avatar?.setOnClickListener { showProfileScreen() }
        viewModel.profileLiveData.observe(this, Observer<UserProfile> { topLeftViewBinding?.userProfile = it })
    }

    override fun showCheckQualityScreen() {
        enableBackButton = true
        baseBinding.viewModel?.enableTopLogo?.set(false)
        title = getString(R.string.home_menu_check_quality)
        openFragment(QualityFragment(), R.id.contentHolder)
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
        openFragment(SaleFragment(), R.id.contentHolder)
    }

    override fun showHomeScreen() {
        enableBackButton = false
        baseBinding.viewModel?.enableTopLogo?.set(true)
        openFragment(HomeFragment(), R.id.contentHolder)
    }

    override fun showHistoryScreen() {
        enableBackButton = false
        baseBinding.viewModel?.enableTopLogo?.set(true)
        openFragment(HistoryFragment(), R.id.contentHolder)
    }


    override fun showProfileScreen() {
        enableBackButton = true
        baseBinding.viewModel?.enableTopLogo?.set(true)
        openFragment(ProfileFragment(), R.id.contentHolder)
    }

    override fun showOrderDetailScreen(dataResponse: Order) {
        enableBackButton = true
        openFragment(SaleDetailFragment.newInstance(dataResponse), R.id.contentHolder)
    }

    override fun showSellNowScreen() {
        enableBackButton = true
        baseBinding.viewModel?.enableTopLogo?.set(false)
        title = getString(R.string.home_menu_sell_now)
        openFragment(SaleNowFragment(), R.id.contentHolder)
    }

    override fun showAddContainerScreen(dataResponse: Order) {
        val mIntent = Intent(this, QualityAddContainer::class.java)
        mIntent.putExtra("EXTRA_ORDER", dataResponse)
        startActivityForResult(mIntent, 500)
        showCheckQualityScreen()
    }

    override fun openFragment(fragment: Fragment, placeHolder: Int, tag: String) {
        currentFragment = fragment
        super.openFragment(fragment, placeHolder, tag)
    }

    override fun onBackPressed() {
        if (currentFragment != null && currentFragment !is HomeFragment) {
            showHomeScreen()
            return
        }
        super.onBackPressed()
    }

    private fun getData() {
//        Handler().postDelayed({
//            ServiceHelper.createService(ApiHelper::class.java).getData().enqueue(object : retrofit2.Callback<ResponseBody> {
//                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                }
//
//                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                    val resultString = response.body()?.string()
//                    val document = Jsoup.parse(resultString).select("tr")
//                    val sensorData = SensorData()
//                    document.forEach { info ->
//                        when {
//                            info.select("td")[0].html().toLowerCase().contains("tilt") -> sensorData.tilt = info.select("td")[1].html()
//                            info.select("td")[0].html().toLowerCase().contains("temp") -> sensorData.temperature = info.select("td")[1].html()
//                            info.select("td")[0].html().toLowerCase().contains("battery") -> sensorData.battery = info.select("td")[1].html()
//                            info.select("td")[0].html().toLowerCase().contains("gravity") -> sensorData.gravity = info.select("td")[1].html()
//                        }
//                    }
//                    tvTilt.text = sensorData.tilt
//                    tvTemp.text = sensorData.temperature
//                    tvBattery.text = sensorData.battery
//                    tvGravity.text = sensorData.gravity
//                }
//            })
//            getData()
//        }, 5000)
    }
}
