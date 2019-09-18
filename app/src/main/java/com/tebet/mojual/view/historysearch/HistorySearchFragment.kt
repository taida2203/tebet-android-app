package com.tebet.mojual.view.historysearch

import android.os.Bundle
import android.view.View
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProviders
import com.tebet.mojual.R
import com.tebet.mojual.databinding.FragmentHistorySearchBinding
import com.tebet.mojual.view.base.BaseFragment
import com.tebet.mojual.view.home.Home

class HistorySearchFragment : BaseFragment<FragmentHistorySearchBinding, HistorySearchViewModel>(),
    HistorySearchNavigator {
    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: HistorySearchViewModel
        get() = ViewModelProviders.of(this, factory).get(HistorySearchViewModel::class.java)

    override val layoutId: Int
        get() = R.layout.fragment_history_search

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator = this
        viewModel.loadData(false)
    }


}
