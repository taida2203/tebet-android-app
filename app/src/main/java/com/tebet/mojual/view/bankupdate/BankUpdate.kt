package com.tebet.mojual.view.bankupdate

import android.app.Activity
import android.os.Bundle
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tebet.mojual.R
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.databinding.ActivityBankUpdateBinding
import com.tebet.mojual.view.base.BaseActivity
import com.tebet.mojual.view.signup.step3.SignUpInfoStep3
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class BankUpdate : BaseActivity<ActivityBankUpdateBinding, BankUpdateViewModel>(),
    BankUpdateNavigator, HasSupportFragmentInjector {
    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: BankUpdateViewModel
        get() = ViewModelProviders.of(this, factory).get(BankUpdateViewModel::class.java)

    override val contentLayoutId: Int
        get() = R.layout.activity_bank_update

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
        viewModel.navigator = this
        title = getString(R.string.bank_update_title)
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
