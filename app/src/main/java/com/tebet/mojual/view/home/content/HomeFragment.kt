package com.tebet.mojual.view.home.content

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.tebet.mojual.BR
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
}
