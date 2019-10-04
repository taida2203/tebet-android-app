package com.tebet.mojual.view.qualitydetail

import android.app.Activity
import android.os.Bundle
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tebet.mojual.R
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.databinding.ActivityOrderDetailBinding
import com.tebet.mojual.view.base.BaseActivity
import com.tebet.mojual.view.signup.step3.SignUpInfoStep3
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class OrderDetailActivity : BaseActivity<ActivityOrderDetailBinding, OrderDetailActivityViewModel>(),
    OrderDetailActivityNavigator, HasSupportFragmentInjector {
    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: OrderDetailActivityViewModel
        get() = ViewModelProviders.of(this, factory).get(OrderDetailActivityViewModel::class.java)

    override val contentLayoutId: Int
        get() = R.layout.activity_order_detail

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
        viewModel.navigator = this
        title = getString(R.string.order_detail_title)
        val currentFragment = SignUpInfoStep3()
        viewModel.loadData()
        openFragment(currentFragment, R.id.placeHolderChild)
        viewModel.userProfileLiveData.observe(this, Observer<UserProfile> { userProfile ->
            currentFragment.viewModel.userProfile.set(userProfile)
        })
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> =
        fragmentDispatchingAndroidInjector

    override fun openPreviousScreen() {
        setResult(Activity.RESULT_OK)
        finish()
    }

}
