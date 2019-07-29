package com.tebet.mojual.view.home.content

import androidx.lifecycle.ViewModelProviders
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.databinding.FragmentHomeBinding
import com.tebet.mojual.view.base.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeContentViewModel>() {
    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: HomeContentViewModel
        get() = ViewModelProviders.of(this, factory).get(HomeContentViewModel::class.java)

    override val layoutId: Int
        get() = R.layout.fragment_home
}
